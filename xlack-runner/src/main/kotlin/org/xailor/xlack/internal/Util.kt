package org.xailor.xlack.internal

import java.io.File

object Util {
  val runtimeDir: File get() = File(Util::class.java.protectionDomain.codeSource.location.path).parentFile
}
