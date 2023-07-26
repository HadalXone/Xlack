package org.xailor.xlack.demoplugin

import com.slack.api.bolt.App
import org.xailor.xlack.XlackFeature
import org.xailor.xlack.interactive.XlackInteractive
import org.xailor.xlack.message.textMessage

class DemoFeature : XlackFeature, XlackInteractive {

  override fun configure(app: App) {
    app.message(".*hello.*".toPattern()) { _, context ->
      textMessage("Hi").post(context.channelId)
      context.ack()
    }
  }
}
