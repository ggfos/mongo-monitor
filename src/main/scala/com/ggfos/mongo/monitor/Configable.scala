package com.ggfos.mongo.monitor

import com.typesafe.config.ConfigFactory

/**
 * Created by primos on 14-9-12.
 */
trait Confable {
  val conf = ConfigFactory.load("properties.conf")

  protected def extractStringProperty(path: String) = conf.getString(path)

  protected def extractIntProperty(path: String) = conf.getInt(path)

  protected def extractBooleanProperty(path: String) = conf.getBoolean(path)

  protected def extractStringProperties(path: String) = conf.getString(path).split(",").toList
}

trait ConfigProperties extends Confable {

  val interval = extractIntProperty("monitor.server.interval")

  val debug = extractBooleanProperty("monitor.server.debug")

  val addresses = extractStringProperties("monitor.server.address")

  val recipients = extractStringProperty("monitor.server.recipients")

  val db = extractStringProperty("monitor.server.db")

  val collection = extractStringProperty("monitor.server.collection")

  val host = extractStringProperty("monitor.mail.host")

  val username = extractStringProperty("monitor.mail.username")

  val password = extractStringProperty("monitor.mail.password")

  val mimeType = extractStringProperty("monitor.mail.mimeType")
}


