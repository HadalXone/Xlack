package org.xeafarer.demoplugin

import com.slack.api.bolt.App
import org.arxing.xlack.XlackFeature
import org.arxing.xlack.interactive.XlackInteractive
import org.arxing.xlack.message.textMessage

class DemoFeature : XlackFeature, XlackInteractive {

  override fun configure(app: App) {
    app.message(".*hello.*".toPattern()) { _, context ->
      textMessage("Hi").post(context.channelId)
      context.ack()
    }
  }
}
