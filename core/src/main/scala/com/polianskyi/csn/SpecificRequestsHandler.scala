package com.polianskyi.csn

import akka.actor.{Actor, Props}

import scala.concurrent.ExecutionContextExecutor
import com.polianskyi.csn.dao.SpecificRequestsDao._

object SpecificRequestsHandler {

  def props(): Props = Props(new CoffeeHouseHandler())

  case class GetSuppliersByProductAndCoffeeHouse(productName: String, address: String)
  case class GetEmployeesByPositionAndCoffeeHouseAndDate(productName: String, address: String, date: Long)
  case class GetGoodsByProductsAndSalesReportAndCoffeeHouseAndDate(address: String, date: Long)
  case class GetCoffeeHouseByProductAndMaxSalesReportsAndDate(productName: String, date: Long)
  case class GetContractByEmployeeBirthdayAndSalary(date: Long, salary: Double)
  case class GetSalesReportByEmployeeAndDate(employee: String, date: Long)
  case class GetPositionBySalaryAndCoffeeHouse(salary: Double, address: String)
  case class GetEmployeeByBirthdayFirstAndBirthdaySecondAndContractDate(firstDate: Long, secondDate: Long, contractDate: Long)
  case class GetCoffeeHouseByMaxSalesReportsAndFromDate(fromDate: Long)
  case class GetEmployeeByCoffeeHouseAndMaxSalesReport(address: String, fromDate: Long)

}


class SpecificRequestsHandler extends Actor {
  import SpecificRequestsHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case GetSuppliersByProductAndCoffeeHouse(productName, address) =>
      val _sender = sender()
      getSuppliersByProductAndCoffeeHouse(productName, address).foreach {
        case Some(i) => _sender ! i
      }


    case GetEmployeesByPositionAndCoffeeHouseAndDate(productName, address, date) =>
      val _sender = sender()
      getEmployeesByPositionAndCoffeeHouseAndDate(productName, address, date).foreach {
        case Some(i) => _sender ! i
      }

    case GetGoodsByProductsAndSalesReportAndCoffeeHouseAndDate(address, date) =>
      val _sender = sender()
      getGoodsByProductsAndSalesReportAndCoffeeHouseAndDate(address, date).foreach {
        case Some(i) => _sender ! i
      }

    case GetCoffeeHouseByProductAndMaxSalesReportsAndDate(productName, date) =>
      val _sender = sender()
      getCoffeeHouseByProductAndMaxSalesReportsAndDate(productName, date).foreach {
        case Some(i) => _sender ! i
      }

    case GetContractByEmployeeBirthdayAndSalary(date, salary) =>
      val _sender = sender()
      getContractByEmployeeAgeAndSalary(date, salary).foreach {
        case Some(i) => _sender ! i
      }

    case GetSalesReportByEmployeeAndDate(employee, date) =>
      val _sender = sender()
      getSalesReportByEmployeeAndDate(employee, date).foreach {
        case Some(i) => _sender ! i
      }

    case GetPositionBySalaryAndCoffeeHouse(salary, address) =>
      val _sender = sender()
      getPositionBySalaryAndCoffeeHouse(salary, address).foreach {
        case Some(i) => _sender ! i
      }

    case GetEmployeeByBirthdayFirstAndBirthdaySecondAndContractDate(firstDate, secondDate, contractDate) =>
      val _sender = sender()
      getEmployeeByBirthdayFirstAndBirthdaySecondAndContractDate(firstDate, secondDate, contractDate).foreach {
        case Some(i) => _sender ! i
      }

    case GetCoffeeHouseByMaxSalesReportsAndFromDate(fromDate) =>
      val _sender = sender()
      getCoffeeHouseByMaxSalesReportsAndFromDate(fromDate).foreach {
        case Some(i) => _sender ! i
      }

    case GetEmployeeByCoffeeHouseAndMaxSalesReport(address, fromDate) =>
      val _sender = sender()
      getEmployeeByCoffeeHouseAndMaxSalesReport(address, fromDate).foreach {
        case Some(i) => _sender ! i
      }


  }
}
