package com.ggfos.mongo.monitor

object Test extends App {
  println("start")
  Monitor.subscribe("192.168.1.9:2000", 60, true, db = "test", collection = "test")
  //  Monitor.subscribe("192.168.1.9:30000", 60, true)
}