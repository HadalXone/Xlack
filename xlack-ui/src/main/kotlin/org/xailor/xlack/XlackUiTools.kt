package org.xailor.xlack

import com.slack.api.Slack
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object XlackUiTools : KoinComponent {
  private val config: XlackConfig by inject()

  val slack: Slack by inject()

  val botUserToken: String get() = config.botUserToken
}
