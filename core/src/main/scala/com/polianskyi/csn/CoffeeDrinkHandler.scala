package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.CoffeeDrinkDao._
import com.polianskyi.csn.domain.{CoffeeDrink, Product}

import scala.concurrent.ExecutionContextExecutor
object CoffeeDrinkHandler {

  def props(): Props = Props(new CoffeeDrinkHandler())

  case class Create(name: String, price: Double, nativePrice: Double, products: List[Product], description: String)
  case class Update(name: String, price: Double, nativePrice: Double, products: List[Product], description: String)
  case class GetCoffeeDrink(name: String)
  case class GetAllCoffeeDrinks()
  case class GetAllCoffeeDrinkAddresses()
  case class DeleteCoffeeDrink(name: String)
  case class CoffeeDrinkNotFound(name: String)
  case class CoffeeDrinkDeleted(name: String)
  case class CoffeeDrinkCreatingError(name: String, message: String)
}

class CoffeeDrinkHandler extends Actor {
  import CoffeeDrinkHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(name, price, nativePrice, products, description) =>
      val _sender = sender()
      create(CoffeeDrink(name, price, nativePrice, products, description)).foreach {
        case Some(i) => _sender ! CoffeeDrink(i.name, i.price, i.nativePrice, i.products, i.description)
        case None => _sender ! CoffeeDrinkCreatingError(name, "Can't create coffee drink")
      }

    case Update(name, price, nativePrice, products, description) => update(CoffeeDrink(name, price, nativePrice, products, description)) pipeTo sender()

    case GetAllCoffeeDrinks() =>
      val _sender = sender()
      findAll().foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case GetCoffeeDrink(name) =>
      val _sender = sender()
      findByPk(name).foreach {
        case Some(i) => _sender ! CoffeeDrink(i.name, i.price, i.nativePrice, i.products, i.description)
        case None => _sender ! CoffeeDrinkNotFound(name)
      }

    case DeleteCoffeeDrink(name) =>
      val _sender = sender()
      delete(name).foreach {
        case i if i != null => _sender ! CoffeeDrinkDeleted(name)
        case _ => _sender ! CoffeeDrinkNotFound(name)
      }
  }
}
