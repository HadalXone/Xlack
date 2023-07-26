package org.xailor.xlack.di

import org.xailor.xlack.XlackConfig
import org.koin.dsl.bind
import org.koin.dsl.module

fun configModule(config: XlackConfig) = module {
  single {
    config
  } bind XlackConfig::class
}
