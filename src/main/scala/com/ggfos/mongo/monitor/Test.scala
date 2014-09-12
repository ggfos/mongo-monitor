package com.ggfos.mongo.monitor

import com.typesafe.config.{ConfigFactory, Config}

object Test extends App {
  println("start")
  //  Monitor.subscribe("192.168.1.9:2000", 60, true, db = "test", collection = "test")
  val a = ConfigFactory.load("properties.conf")
  println(a)
  println(a.getInt("monitor.waittime"))
  //  Monitor.subscribe("192.168.1.9:30000", 60, true)
}