package com.ggfos.mongo.monitor

object ShutDownAction extends ThreadConverter {
  def +(action: => Unit) =
    Runtime.getRuntime.addShutdownHook(action)
}