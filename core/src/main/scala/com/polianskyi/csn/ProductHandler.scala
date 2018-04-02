package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.ProductDao._
import com.polianskyi.csn.domain.Product

import scala.concurrent.ExecutionContextExecutor

object ProductHandler {
  def props(): Props = Props(new ProductHandler())

  case class Create(name: String, price: Double, unitOfMeasurement: String, description: String)
  case class Update(name: String, price: Double, unitOfMeasurement: String, description: String)
  case class GetProduct(name: String)
  case class GetAllProducts()
  case class GetAllProductAddresses()
  case class DeleteProduct(name: String)
  case class ProductNotFound(name: String)
  case class ProductDeleted(name: String)
  case class ProductCreatingError(name: String, message: String)
}

class ProductHandler extends Actor {
  import ProductHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(name, price, unitOfMeasurement, description) =>
      val _sender = sender()
      create(Product(name, price, unitOfMeasurement, description)).foreach {
        case Some(i) => _sender ! Product(i.name, i.price, i.unitOfMeasurement, i.description)
        case None => _sender ! ProductCreatingError(name, "Can't create product")
      }

    case Update(name, price, unitOfMeasurement, description) => update(Product(name, price, unitOfMeasurement, description)) pipeTo sender()

    case GetAllProducts() =>
      val _sender = sender()
      findAll().foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case GetProduct(name) =>
      val _sender = sender()
      findByPk(name).foreach {
        case Some(i) => _sender ! Product(i.name, i.price, i.unitOfMeasurement, i.description)
        case None => _sender ! ProductNotFound(name)
      }

    case DeleteProduct(name) =>
      val _sender = sender()
      delete(name).foreach {
        case i if i != null => _sender ! ProductDeleted(name)
        case _ => _sender ! ProductNotFound(name)
      }
  }
}
