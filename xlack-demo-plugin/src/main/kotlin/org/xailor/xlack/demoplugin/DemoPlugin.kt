package org.xailor.xlack.demoplugin

import org.xailor.xlack.XlackFeature
import org.xailor.xlack.XlackPlugin
import org.xailor.xlack.interactive.XlackInteractive

class DemoPlugin : XlackPlugin, XlackInteractive {

  override val pluginName: String = "DemoPlugin"

  override fun initialize() {
  }

  override fun defineFeatures(): List<XlackFeature> {
    return listOf(DemoFeature())
  }
}
