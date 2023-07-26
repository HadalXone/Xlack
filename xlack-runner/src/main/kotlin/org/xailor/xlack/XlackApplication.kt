package org.xailor.xlack

import org.xailor.xlack.command.XlackCommand
import org.xailor.xlack.di.runnerModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.INFO
import org.koin.core.logger.PrintLogger

object XlackApplication : KoinComponent {
  val config: XlackConfig by inject()

  fun main(args: Array<String>) {
    setupDI()
    XlackCommand().main(args)
  }

  private fun setupDI() {
    startKoin {
      logger(PrintLogger(INFO))
      modules(
        runnerModule(),
      )
    }
  }
}
