package com.ggfos.common

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
