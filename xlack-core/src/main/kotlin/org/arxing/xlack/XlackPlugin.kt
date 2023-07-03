package org.arxing.xlack

interface XlackPlugin {

  val pluginName: String

  fun initialize()

  fun defineFeatures(): List<XlackFeature>
}
