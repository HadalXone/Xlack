package org.arxing.xlack.di

import org.arxing.xlack.XlackConfig
import org.koin.dsl.bind
import org.koin.dsl.module

fun configModule(config: XlackConfig) = module {
  single {
    config
  } bind XlackConfig::class
}
