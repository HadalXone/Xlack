package org.xeafarer.demoplugin

import org.arxing.xlack.XlackFeature
import org.arxing.xlack.XlackPlugin
import org.arxing.xlack.interactive.XlackInteractive

class DemoPlugin : XlackPlugin, XlackInteractive {

  override val pluginName: String = "DemoPlugin"

  override fun initialize() {
  }

  override fun defineFeatures(): List<XlackFeature> {
    return listOf(DemoFeature())
  }
}
