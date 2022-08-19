package org.arxing.xlack.internal

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.arxing.xlack.XlackConfig
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
class SlackApiMonitor(
  private val config: XlackConfig,
) : Interceptor {
  private val logger by logging("SlackApiMonitor")

  override fun intercept(chain: Chain): Response {
    val request = hookLoggingRequest(chain.request())
    val (response, duration) = measureTimedValue {
      chain.proceed(request)
    }
    return hookLoggingResponse(request.method, request.url, response, duration)
  }

  private fun hookLoggingRequest(request: Request): Request {
    val originBody = request.body
    var bodyString: String? = null
    val restoreBody = if (originBody != null) {
      val contentType = originBody.contentType()
      val content = Buffer().apply { originBody.writeTo(this) }.readString(Charsets.UTF_8)
      bodyString = content
      content.toRequestBody(contentType)
    } else null

    if (config.logRequest) {
      logger.info("${"Request".bg(AnsiColor.Yellow)} [${request.method}] ${request.url}\nbody=>$bodyString")
    }
    return request.newBuilder().method(request.method, restoreBody).build()
  }

  private fun hookLoggingResponse(method: String, url: HttpUrl, response: Response, duration: Duration): Response {
    val originBody = response.body
    var bodyJson: JsonObject? = null
    var bodyString: String? = null
    val restoreBody = if (originBody != null) {
      val contentType = originBody.contentType()
      val content = originBody.string()
      bodyJson = JsonParser.parseString(content).asJsonObject
      bodyString = GsonBuilder()
        .let { if (config.prettyJson) it.setPrettyPrinting() else it }
        .create()
        .toJson(bodyJson)
      content.toResponseBody(contentType)
    } else null

    val ok = bodyJson?.get("ok")?.asBoolean == false
    if (config.logResponse && (!config.logWhenError || ok)) {
      val costMills = duration.inWholeMilliseconds
      val bgColor = if (ok) AnsiColor.Green else AnsiColor.Red
      logger.info("${"Response".bg(bgColor)} [${method}][${costMills}ms] ${url}\nbody=>$bodyString")
    }
    return response.newBuilder().body(restoreBody).build()
  }
}
