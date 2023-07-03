package org.arxing.xlack.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.google.gson.Gson
import org.arxing.xlack.XlackApplication
import org.arxing.xlack.di.configModule
import org.arxing.xlack.di.slackModule
import org.arxing.xlack.internal.Util
import org.arxing.xlack.XlackConfig
import org.arxing.xlack.internal.logging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class XlackCommand : CliktCommand(), KoinComponent {
  private val logger by logging()
  private val gson: Gson by inject()
  private val configFile: File by option("--config")
    .file(canBeDir = false)
    .defaultLazy { File(Util.runtimeDir, "xlack-config.json") }

  override fun run() {
    loadConfig()
  }

  private fun loadConfig() {
    logger.info("Loading xlack config...")
    if (!configFile.exists()) {
      val content = gson.toJson(XlackConfig.emptyConfig())
      configFile.writeText(content)
    }
    XlackApplication.getKoin().loadModules(
      listOf(
        configModule(gson.fromJson(configFile.readText(), XlackConfig::class.java)),
        slackModule(),
      )
    )
  }
}
