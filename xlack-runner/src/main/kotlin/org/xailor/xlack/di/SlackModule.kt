package org.xailor.xlack.di

import com.slack.api.Slack
import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.util.SlackRequestParser
import com.slack.api.util.http.SlackHttpClient
import okhttp3.OkHttpClient
import org.xailor.xlack.XlackConfig
import org.xailor.xlack.internal.SlackApiMonitor
import org.koin.dsl.bind
import org.koin.dsl.module

fun slackModule() = module {
  single {
    val xlackConfig = get<XlackConfig>()
    val botUserToken = xlackConfig.botUserToken
    val signingSecret = xlackConfig.signingSecret
    check(botUserToken.isNotBlank()) { "Missing bot_user_token" }
    check(signingSecret.isNotBlank()) { "Missing signing_secret" }
    AppConfig.builder()
      .singleTeamBotToken(botUserToken)
      .signingSecret(signingSecret)
      .build()
  } bind AppConfig::class

  single {
    val appConfig: AppConfig = get()
    App(appConfig)
  } bind App::class

  single {
    val appConfig: AppConfig = get()
    SlackRequestParser(appConfig)
  } bind SlackRequestParser::class

  single {
    val config = get<XlackConfig>()
    OkHttpClient.Builder()
      .addInterceptor(SlackApiMonitor(config))
      .build()
  } bind OkHttpClient::class

  single {
    val okHttpClient: OkHttpClient = get()
    SlackHttpClient(okHttpClient)
  } bind SlackHttpClient::class

  single {
    val slackHttpClient: SlackHttpClient = get()
    Slack.getInstance(slackHttpClient)
  } bind Slack::class
}
