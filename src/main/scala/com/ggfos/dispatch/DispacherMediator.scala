package com.ggfos.dispatch

/**
 * Created by primos on 14-9-12.
 */
trait DispacherMediator {
  def sendNodify(address: String, msg: String) = {
//    MailDispacher.send(address, msg)
    SmsDispacher.send(address, msg)
  }
}
