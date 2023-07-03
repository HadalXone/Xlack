package org.arxing.xlack.cli.internal

import java.security.Permission

internal fun <R> runWithRedirectExitToThrowable(block: () -> R): Result<R> {
  val originSecurityManager = System.getSecurityManager()
  System.setSecurityManager(RedirectExitToThrowableSecurityManager())
  val result = runCatching(block)
  System.setSecurityManager(originSecurityManager)
  return result
}

internal class RedirectExitToThrowableSecurityManager : SecurityManager() {

  override fun checkPermission(perm: Permission?) = Unit

  override fun checkExit(status: Int): Unit = throw ExitException()
}

internal class ExitException : Throwable()
