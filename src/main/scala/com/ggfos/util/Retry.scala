package com.ggfos.util

/**
 * Created by primos on 14-9-12.
 */
object Retry {
  def apply[R](tries: => R, catches: Throwable => Unit = {
    e => e.printStackTrace()
  }, finalDo: => Unit = {})(isOk: Option[R] => Boolean = {
    o: Option[R] => None != o
  }) = {
    var r: Option[R] = None
    while (!isOk(r))
      try {
        r = Option(tries)
      } catch {
        case e: Throwable => catches(e)
      } finally {
        finalDo
      }

    r.get
  }
}
