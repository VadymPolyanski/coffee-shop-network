package com.polianskyi.csn

import akka.actor.{Actor, Props}
import com.polianskyi.csn.dao.PositionDao._

import scala.concurrent.ExecutionContextExecutor

object PositionHandler {
  def props(): Props = Props(new PositionHandler())

  case class GetAllPositions()
}


class PositionHandler extends Actor {
  import PositionHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case GetAllPositions() =>
      val _sender = sender()
      findAll().foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

  }
}
