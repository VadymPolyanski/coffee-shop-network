package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.CoffeeHouseDao._
import com.polianskyi.csn.domain.CoffeeHouse
import scala.concurrent.ExecutionContextExecutor

object CoffeeHouseHandler {

  def props(): Props = Props(new CoffeeHouseHandler())

  case class Create(address: String, space: Double, rentalPrice: Double, mobileNumber: String)
  case class Update(address: String, space: Double, rentalPrice: Double, mobileNumber: String)
  case class GetCoffeeHouse(address: String)
  case class DeleteCoffeeHouse(address: String)
  case class CoffeeHouseNotFound(address: String)
  case class CoffeeHouseDeleted(address: String)
}

class CoffeeHouseHandler extends Actor {
  import CoffeeHouseHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(address, space, rentalPrice, mobileNumber) => create(CoffeeHouse(address, space, rentalPrice, mobileNumber)) pipeTo sender()

    case Update(address, space, rentalPrice, mobileNumber) => update(CoffeeHouse(address, space, rentalPrice, mobileNumber)) pipeTo sender()


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
        case _ => _sender ! CoffeeHouseNotFound(address)
      }
  }
}
