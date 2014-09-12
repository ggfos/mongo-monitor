package com.ggfos.util

object ShutDownAction extends ThreadConverter {
  def +(action: => Unit) =
    Runtime.getRuntime.addShutdownHook(action)
}