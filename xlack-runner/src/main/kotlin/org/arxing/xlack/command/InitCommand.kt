package org.arxing.xlack.command

import com.github.ajalt.clikt.core.CliktCommand
import org.koin.core.component.KoinComponent

class InitCommand : CliktCommand(name = "init"), KoinComponent {

  override fun run() {
  }
}
