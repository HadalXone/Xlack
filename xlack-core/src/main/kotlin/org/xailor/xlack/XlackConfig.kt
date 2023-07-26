package org.xailor.xlack

import com.google.gson.annotations.SerializedName

data class XlackConfig(
  @SerializedName("client_secret") val clientSecret: String = "",
  @SerializedName("signing_secret") val signingSecret: String = "",
  @SerializedName("port") val port: Int = 8080,
  @SerializedName("bot_user_token") val botUserToken: String = "",
  @SerializedName("app_token") val appToken: String = "",
  @SerializedName("log_request") val logRequest: Boolean = true,
  @SerializedName("log_response") val logResponse: Boolean = true,
  @SerializedName("log_when_error") val logWhenError: Boolean = true,
  @SerializedName("pretty_json") val prettyJson: Boolean = false,
) {

  companion object {

    fun emptyConfig(): XlackConfig = XlackConfig()
  }
}
