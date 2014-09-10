package com.ggfos.mongo.monitor

import scala.annotation.implicitNotFound

trait ImplicitCookies extends WeekBooleanCookies {
  protected implicit def anyToOption[R](x: R)(implicit ev: Null <:< R) = Option(x)
  type Str = String
}

trait WeekBooleanCookies {
  protected implicit def iteratorToBoolean(x: Iterator[_]) = x != null && !x.isEmpty
  protected implicit def iterableToBoolean(x: Iterable[_]) = x != null && !x.isEmpty
  protected implicit def stringToBoolean(x: String) = x != null && !x.isEmpty
  protected implicit def numberToBoolean[N <% Number](x: N) = x != null && x.intValue != 0
  protected implicit def arrayToBoolean(x: Array[_]) = x != null && !x.isEmpty
}
