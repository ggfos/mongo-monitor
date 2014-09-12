package com.ggfos.dispatch

import com.ggfos.common.ConfigProperties
import com.joyrec.util.http.{HttpParameterUtil, Httpable}

/**
 * Created by primos on 14-9-12.
 */
object SmsDispacher extends ConfigProperties with Httpable {
  val basicReqUrl = "https://rest.nexmo.com/sms/json"

  def send(errAddress: String, msg: String) = to.foreach {
    t =>
      println(t)
      val params = Array("api_key" -> appKey,
        "api_secret" -> appSercret,
        "from" -> from,
        "to" -> t,
        "type" -> "unicode",
        "text" -> HttpParameterUtil.urlEncode(s"$errAddress : $msg"))
      println(get(basicReqUrl, params: _*)())
  }

}
