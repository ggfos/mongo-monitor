package com.ggfos.util

import java.util.concurrent.Callable
import java.util.concurrent.Executors

import scala.util.control.Breaks.breakable
import java.util.concurrent.atomic.AtomicInteger

object Threads {
  lazy val pool = Executors.newCachedThreadPool

  private var shutdownToken = false
  ShutDownAction + {
    shutdownToken = true
    pool.shutdown
  }

  def apply[R](fn: => R) =
    pool submit new Callable[R] {
      def call = fn
    }

  def aplly(fn: => Unit) =
    pool execute new Runnable {
      def run = fn
    }

  /**
   * 生产消费//非精准
   */
  def takeCost[TASK](/*最大库存*/ maxStock: Int, maxHander: Int,
                     hasTask: => Boolean = true, lock: Object = new Object)(process: => TASK)(cost: TASK => Unit) = {
    val curStock = new AtomicInteger
    val curHander = new AtomicInteger
    breakable {
      while (hasTask && !shutdownToken) {
        //println(s"task=$lock curStock=$curStock curHander=$curHander")
        if (curHander.get >= maxHander || curStock.get >= maxStock)
          lock.synchronized {
            lock.wait(5000)
          }
        else {
          val task = process
          curStock.incrementAndGet
          apply {
            curHander.incrementAndGet
            try {
              cost(task)
            } catch {
              case e: Throwable => e.printStackTrace
            } finally {
              curStock.decrementAndGet
              curHander.decrementAndGet
            }
          }
        }
      }
    }

  }
}


