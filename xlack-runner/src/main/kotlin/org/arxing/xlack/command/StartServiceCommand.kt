package org.arxing.xlack.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import com.slack.api.bolt.util.SlackRequestParser
import io.ktor.serialization.gson.gson
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.arxing.xlack.XlackApplication
import org.arxing.xlack.XlackFeature
import org.arxing.xlack.XlackPluginManager
import org.arxing.xlack.internal.SlackRouter
import org.arxing.xlack.internal.Util
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class StartServiceCommand : CliktCommand(name = "start"), KoinComponent {
  private val pluginManager: XlackPluginManager by inject()

  private val pluginsDir: File by option("--plugins")
    .file(canBeFile = false)
    .defaultLazy { File(Util.runtimeDir, "plugins") }

  private val isHttpMode: Boolean by option("--http")
    .flag(default = false)

  private val slackApp: App by inject()
  private val requestParser: SlackRequestParser by inject()

  override fun run() {
    pluginManager.loadPlugins(pluginsDir)
    pluginManager.features.forEach registerFeatures@{ feature ->
      slackApp.registerFeature(feature)
    }

    if (isHttpMode) {
      startWithHttpMode()
    } else {
      startWithSocketMode()
    }
  }

  private fun startWithHttpMode() {
    embeddedServer(Netty, port = XlackApplication.config.port) {
      install(CallLogging)
      install(ContentNegotiation) {
        gson { setPrettyPrinting() }
      }

      routing {
        val slackRouter = SlackRouter(slackApp, requestParser)
        slackRouter.initialize(this)
      }
    }.start(wait = true)
  }

  private fun startWithSocketMode() {
    val appToken = XlackApplication.config.appToken
    check(appToken.isNotBlank()) { "Missing app_token" }
    val socketModeApp = SocketModeApp(appToken, slackApp)
    socketModeApp.start()
  }

  private fun App.registerFeature(feature: XlackFeature) {
    feature.configure(this)
  }
}
