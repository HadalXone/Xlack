package org.arxing.xlack.internal

import com.google.gson.JsonParser
import com.slack.api.bolt.App
import com.slack.api.bolt.ktor.respond
import com.slack.api.bolt.ktor.toBoltRequest
import com.slack.api.bolt.util.SlackRequestParser
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import kotlinx.coroutines.delay

class SlackRouter(
  private val slackApp: App,
  private val requestParser: SlackRequestParser,
) {

  fun initialize(routing: Routing) {
    with(routing) {
      post("/slack/events") {
        val boltRequest = toBoltRequest(call, requestParser)
        val boltResponse = slackApp.run(boltRequest)
        respond(call, boltResponse)
      }

      post("/slack/challenge") {
        kotlin.runCatching {
          val json = JsonParser.parseString(call.receiveText()).asJsonObject
          val challenge = json["challenge"]
          checkNotNull(challenge)
          check(challenge.asJsonPrimitive.isString)
          challenge.asString
        }.onSuccess {
          call.respondText(
            contentType = ContentType.parse("text/plain"),
            status = HttpStatusCode.OK,
            text = it,
          )
        }.onFailure {
          call.respondText(
            contentType = ContentType.parse("text/plain"),
            status = HttpStatusCode.BadRequest,
            text = it.message.orEmpty(),
          )
        }
      }
    }
  }
}
