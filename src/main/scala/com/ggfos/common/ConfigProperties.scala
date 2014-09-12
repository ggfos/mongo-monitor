package com.ggfos.common

/**
 * Created by primos on 14-9-12.
 */

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

  val appKey = extractStringProperty("monitor.sms.appkey")

  val appSercret = extractStringProperty("monitor.sms.appsercret")

  val from = extractStringProperty("monitor.sms.from")

  val to = extractStringProperties("monitor.sms.to")
}


