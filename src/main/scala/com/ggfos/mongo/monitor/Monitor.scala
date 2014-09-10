package com.ggfos.mongo.monitor

import java.util.HashMap

import com.mongodb.Mongo
import com.mongodb.MongoOptions

object Monitor extends App with MailDispatch with ImplicitCookies {
  var isShutDown = false
  ShutDownAction + { isShutDown = true }

  def subscribe(serverAddress: String, waitTime: Long, debug: Boolean = false, recipient: String = "335365344@qq.com", db: String = "test", collection: String = "test") = {
    if (waitTime) Record.waitTime = waitTime * 1000
    val dbConnections = serverAddress.split(",").toList.map {
      address => (address, DB(address))
    }.toMap
    Threads.takeCost(1, 1, !isShutDown)(process = serverAddress.split(",").toList) {
      case address =>
        address.map { addr =>
          val reader = dbConnections(addr)
          try {
            reader.getDB(db).getCollection(collection).getStats().toString()
            if (debug) println(reader.getDB(db).getCollection(collection).getStats().toString())
          } catch {
            case e: Throwable =>
              if (System.currentTimeMillis - Record.get(s"$recipient~$addr") > Record.waitTime) {
                if (debug) println(s"$recipient~$addr", e)
                Record.put(s"$recipient~$addr", System.currentTimeMillis)
                send("辛苦的值班人员", recipient, addr, s"""$e""")
              } else { this.synchronized { this.wait(Record.waitTime) } }
          }
        }
    }
  }

  def start(recipient: String = "335365344@qq.com", serverAddress: String, waitTime: Long) = {
    if (waitTime) Record.waitTime = waitTime * 1000
    val dbConnections = serverAddress.split(",").toList.map {
      address => (address, DB(address))
    }.toMap
    Threads.takeCost(1, 1, !isShutDown)(process = serverAddress.split(",").toList) {
      case address =>
        address.map { addr =>
          val reader = dbConnections(addr)
          try {
            reader.getDB("test").getCollection("test").getStats().toString()
          } catch {
            case e: Throwable =>
              if (System.currentTimeMillis - Record.get(s"$recipient~$addr") > Record.waitTime) {
                println(s"$recipient~$addr", e)
                Record.put(s"$recipient~$addr", System.currentTimeMillis)
                send("周末辛苦的值班人员", recipient, addr, s"""$e""")
              } else { this.synchronized { this.wait(Record.waitTime) } }
          }
        }
    }
  }
  object DB {
    def apply(address: String) = {
      val option = new MongoOptions
      option.autoConnectRetry = true
      option.safe = true
      option.slaveOk = true
      new Mongo(address, option)
    }
  }
  object Record {
    var waitTime = 360000L
    val revAddressTimer: HashMap[String, Long] = new HashMap()
    def get(key: String) = revAddressTimer.get(key)
    def put(key: String, value: Long) = revAddressTimer.put(key, value)
  }
  //  subscribe("192.168.1.9", 60, true)
}
