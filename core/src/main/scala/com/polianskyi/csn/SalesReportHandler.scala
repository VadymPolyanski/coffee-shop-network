package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.SalesReportDao._
import com.polianskyi.csn.domain._

import scala.concurrent.ExecutionContextExecutor
object SalesReportHandler {

  def props(): Props = Props(new SalesReportHandler())

  case class Create(coffeeDrink: CoffeeDrink, employee: Employee, priceWithVat: Double, saleDate: Long, coffeeHouse: CoffeeHouse)
  case class Update(coffeeDrink: CoffeeDrink, employee: Employee, priceWithVat: Double, saleDate: Long, coffeeHouse: CoffeeHouse)
  case class GetSalesReport(pk: SalesReportPK)
  case class GetSalesReportsByCoffeeHouse(address: String)
  case class GetSalesReportsByEmployee(fullName: String)
  case class GetAllSalesReports()
  case class GetAllSalesReportAddresses()
  case class DeleteSalesReport(pk: SalesReportPK)
  case class SalesReportNotFound(pk: SalesReportPK)
  case class SalesReportDeleted(pk: SalesReportPK)
  case class SalesReportCreatingError(pk: SalesReportPK, message: String)
}

class SalesReportHandler extends Actor {
  import SalesReportHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(coffeeDrink, employee, priceWithVat, saleDate, coffeeHouse) =>
      val _sender = sender()
      create(SalesReport(coffeeDrink, employee, priceWithVat, saleDate, coffeeHouse)).foreach {
        case Some(i) => _sender ! SalesReport(i.coffeeDrink, i.employee, i.priceWithVat, i.saleDate, i.coffeeHouse)
        case None => _sender ! SalesReportCreatingError(SalesReportPK(coffeeDrink.name, employee.fullName, saleDate), "Can't create coffee drink")
      }

    case Update(name, price, nativePrice, products, description) => update(SalesReport(name, price, nativePrice, products, description)) pipeTo sender()

    case GetAllSalesReports() =>
      val _sender = sender()
      findAll().foreach {
        case Some(i) => _sender ! i
        case Some(Nil) => Nil
      }

    case GetSalesReportsByCoffeeHouse(address) =>
      val _sender = sender()
      findAllByCoffeeHouse(address).foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case GetSalesReportsByEmployee(fullName) =>
      val _sender = sender()
      findAllByEmployee(fullName).foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case GetSalesReport(name) =>
      val _sender = sender()
      findByPk(name).foreach {
        case Some(i) => _sender ! SalesReport(i.coffeeDrink, i.employee, i.priceWithVat, i.saleDate, i.coffeeHouse)
        case None => _sender ! SalesReportNotFound(name)
      }

    case DeleteSalesReport(name) =>
      val _sender = sender()
      delete(name).foreach {
        case i if i != null => _sender ! SalesReportDeleted(name)
        case _ => _sender ! SalesReportNotFound(name)
      }
  }
}
