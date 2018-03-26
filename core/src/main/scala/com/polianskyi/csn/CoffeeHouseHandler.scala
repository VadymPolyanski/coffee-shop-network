package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.CoffeeHouseDao._
import com.polianskyi.csn.domain.CoffeeHouse
import scala.concurrent.ExecutionContextExecutor

object CoffeeHouseHandler {

  def props(): Props = Props(new CoffeeHouseHandler())

  case class Create(coffeeHouse: CoffeeHouse)
  case class Update(coffeeHouse: CoffeeHouse)
  case class GetCoffeeHouse(address: String)
  case class DeleteCoffeeHouse(address: String)
  case class CoffeeHouseNotFound(username: String)
  case class CoffeeHouseDeleted(username: String)
}

class CoffeeHouseHandler extends Actor {
  import CoffeeHouseHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(coffeeHouse) => create(coffeeHouse) pipeTo sender()

    case Update(coffeeHouse) => update(coffeeHouse) pipeTo sender()

    case GetCoffeeHouse(address) =>
      val _sender = sender()
      findByPk(address).foreach {
        case Some(i) => _sender ! CoffeeHouse(i.address, i.space, i.rentalPrice, i.mobileNumber)
        case None => _sender ! CoffeeHouseNotFound(address)
      }

    case DeleteCoffeeHouse(address) =>
      val _sender = sender()
      delete(address).foreach {
        case i if i != null => _sender ! CoffeeHouseDeleted(address)
      }
  }
}
