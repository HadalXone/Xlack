package org.xailor.xlack.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.xailor.xlack.XlackPluginManager
import org.koin.dsl.bind
import org.koin.dsl.module

fun runnerModule() = module {
  single {
    XlackPluginManager()
  }

  single {
    GsonBuilder()
      .setPrettyPrinting()
      .create()
  } bind Gson::class
}
