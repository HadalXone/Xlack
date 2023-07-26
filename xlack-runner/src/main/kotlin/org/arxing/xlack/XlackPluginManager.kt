package org.arxing.xlack

import org.arxing.xlack.internal.logging
import java.io.File
import java.net.URLClassLoader
import java.util.ServiceLoader

class XlackPluginManager {
  private val logger by logging()
  private val plugins = mutableListOf<XlackPlugin>()

  val features: List<XlackFeature> get() = plugins.flatMap { it.defineFeatures() }

  fun loadPlugins(pluginsDir: File) {
    logger.info("Scan plugins at $pluginsDir...")
    val urls = pluginsDir
      .listFiles { file: File -> file.extension.equals("jar", true) }
      .orEmpty()
      .map { it.toURI().toURL() }
      .toTypedArray()
    val pluginsLoader = URLClassLoader(urls, ClassLoader.getSystemClassLoader())
    plugins.clear()
    ServiceLoader.load(XlackPlugin::class.java, pluginsLoader)
      .onEach { it.initialize() }
      .onEach { logger.info("=> Plugin '${it.pluginName}' was loaded.") }
      .forEach { plugins.add(it) }
    logger.info("Totally loaded ${features.size} features.")
  }
}
