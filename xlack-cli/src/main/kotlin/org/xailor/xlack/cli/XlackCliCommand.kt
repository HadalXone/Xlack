package org.xailor.xlack.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.request.builtin.SlashCommandRequest
import kotlinx.coroutines.runBlocking
import org.xailor.xlack.cli.internal.runWithRedirectExitToThrowable
import org.xailor.xlack.interactive.XlackInteractive
import org.xailor.xlack.message.xlackMessage
import kotlin.properties.Delegates

abstract class XlackCliCommand(
  help: String = "",
  epilog: String = "",
  name: String? = null,
  invokeWithoutSubcommand: Boolean = false,
  printHelpOnEmptyArgs: Boolean = false,
  helpTags: Map<String, String> = emptyMap(),
  autoCompleteEnvvar: String? = "",
  allowMultipleSubcommands: Boolean = false,
  treatUnknownOptionsAsArgs: Boolean = false,
  private val generalColor: String = "#00FF00",
  private val errorColor: String = "#FF0000"
) : CliktCommand(
  help,
  epilog,
  name,
  invokeWithoutSubcommand,
  printHelpOnEmptyArgs,
  helpTags,
  autoCompleteEnvvar,
  allowMultipleSubcommands,
  treatUnknownOptionsAsArgs
), XlackInteractive {
  private var request: SlashCommandRequest by Delegates.notNull()
  private var context: SlashCommandContext by Delegates.notNull()

  protected abstract suspend fun run(request: SlashCommandRequest, context: SlashCommandContext)

  private fun updateContext(request: SlashCommandRequest, context: SlashCommandContext) {
    this.request = request
    this.context = context
  }

  fun setupConsole(channel: String) {
    context {
      console = XlackCliConsole { text, isError ->
        xlackMessage {
          attachments {
            attachment {
              text(text)
              color(if (isError) errorColor else generalColor)
            }
          }
        }.postEphemeral(channel, context.requestUserId)
      }
      // Preventing @argfile expansion
      // see: https://ajalt.github.io/clikt/advanced/#preventing-argfile-expansion
      expandArgumentFiles = false
    }
  }

  fun main(request: SlashCommandRequest, context: SlashCommandContext) {
    updateContext(request, context)
    val args = request.payload.text.split("\\s+".toPattern())

    registeredSubcommands()
      .filterIsInstance<XlackCliCommand>()
      .forEach { it.updateContext(request, context) }

    runWithRedirectExitToThrowable {
      setupConsole(context.channelId)
      main(args)
    }
  }

  final override fun run() {
    runBlocking {
      run(request, context)
    }
  }
}
