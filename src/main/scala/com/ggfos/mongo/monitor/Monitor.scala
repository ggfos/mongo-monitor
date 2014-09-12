package com.ggfos.mongo.monitor

import java.util.HashMap

import com.mongodb.Mongo
import com.mongodb.MongoOptions
import com.ggfos.dispatch._
import com.ggfos.util._
import com.ggfos.common.ConfigProperties

object Monitor extends DispacherMediator with ImplicitCookies with ConfigProperties {
  var isShutDown = false
  ShutDownAction + {
    isShutDown = true
  }

  def subscribe = {
    if (interval) Record.waitTime = interval * 1000
    val dbConnections = addresses.map {
      address => (address, DB(address))
    }.toMap
    Threads.takeCost(1, 1, !isShutDown)(process = addresses) {
      case address =>
        address.map {
          addr =>
            val reader = dbConnections(addr)
            try {
              reader.getDB(db).getCollection(collection).getStats().toString()
              if (debug) println(reader.getDB(db).getCollection(collection).getStats().toString())
            } catch {
              case e: Throwable =>
                if (System.currentTimeMillis - Record.get(s"$recipients~$addr") > Record.waitTime) {
                  if (debug) println(s"$recipients~$addr", e)
                  Record.put(s"$recipients~$addr", System.currentTimeMillis)
                  sendNodify(addr, s"""$e""")
                } else {
                  this.synchronized {
                    this.wait(Record.waitTime)
                  }
                }
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

  def main(args: Array[String]) {
    subscribe
  }
}
