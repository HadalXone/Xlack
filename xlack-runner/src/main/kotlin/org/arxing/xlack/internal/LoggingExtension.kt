package org.arxing.xlack.internal

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty

fun logging(name: String = "Xlack"): ReadOnlyProperty<Any?, Logger> {
  return ReadOnlyProperty { _, _ ->
    LoggerFactory.getLogger(name)
  }
}
