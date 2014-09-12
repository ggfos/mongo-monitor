package com.ggfos.mongo.monitor

import com.typesafe.config.{ConfigFactory, Config}
import com.joyrec.util.http.Httpable

object Test extends App with Httpable {
  //  println("start")
  //  //  Monitor.subscribe("192.168.1.9:2000", 60, true, db = "test", collection = "test")
  //  val a = ConfigFactory.load("properties.conf")
  //  println(a)
  //  println(a.getInt("monitor.waittime"))
  //  //  Monitor.subscribe("192.168.1.9:30000", 60, true)
  val params = Array("a" -> "a")
  println(get("http://www.baidu.com", params: _*)())
}