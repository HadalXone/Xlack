package org.xailor.xlack.cli

import com.github.ajalt.clikt.output.CliktConsole

class XlackCliConsole(
  private val onPrint: (text: String, isError: Boolean) -> Unit,
) : CliktConsole {

  override val lineSeparator: String = System.lineSeparator()

  override fun promptForLine(prompt: String, hideInput: Boolean): String? {
    error("XlackCliConsole does not support promptForLine()")
  }

  override fun print(text: String, error: Boolean) = onPrint(text, error)
}
