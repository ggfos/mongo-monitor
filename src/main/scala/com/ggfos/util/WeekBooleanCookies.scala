package com.ggfos.util

/**
 * Created by primos on 14-9-12.
 */

trait WeekBooleanCookies {
  protected implicit def iteratorToBoolean(x: Iterator[_]) = x != null && !x.isEmpty

  protected implicit def iterableToBoolean(x: Iterable[_]) = x != null && !x.isEmpty

  protected implicit def stringToBoolean(x: String) = x != null && !x.isEmpty

  protected implicit def numberToBoolean[N <% Number](x: N) = x != null && x.intValue != 0

  protected implicit def arrayToBoolean(x: Array[_]) = x != null && !x.isEmpty
}
