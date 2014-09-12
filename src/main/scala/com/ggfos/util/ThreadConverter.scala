package com.ggfos.util

import java.util.concurrent.Callable

/**
 * Created by primos on 14-9-12.
 */
trait ThreadConverter {
  implicit def makeRunnable(f: => Unit): Runnable = new Runnable() {
    def run() = f
  }

  implicit def makeCallable[T](f: => T): Callable[T] = new Callable[T]() {
    def call() = f
  }

  implicit def makeThread(f: => Unit) = new Thread {
    override def run = f
  }
}