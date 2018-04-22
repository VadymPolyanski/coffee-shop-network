package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives.{complete, get, logRequestResult, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.polianskyi.csn.SpecificRequestsHandler._
import com.polianskyi.csn.domain.SalesReport
import com.polianskyi.csn.service.Protocols


trait SpecificRequestsHttpService extends Protocols with GenericHttpService {
  def specificRequestsHandler: ActorRef

  val specificRequestsRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("first") {
            parameters('productName, 'address) { (productName, address) =>
              get {
                complete {
                  (specificRequestsHandler ? GetSuppliersByProductAndCoffeeHouse(productName, address)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("second") {
            parameters('productName, 'address, 'date) { (productName, address, date) =>
              get {
                complete {
                  (specificRequestsHandler ? GetEmployeesByPositionAndCoffeeHouseAndDate(productName, address, date.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("third") {
            parameters('address, 'date) { (address, date) =>
              get {
                complete {
                  (specificRequestsHandler ? GetGoodsByProductsAndSalesReportAndCoffeeHouseAndDate(address, date.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("fourth") {
            parameters('address, 'date) { (address, date) =>
              get {
                complete {
                  (specificRequestsHandler ? GetCoffeeHouseByProductAndMaxSalesReportsAndDate(address, date.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("fifth") {
            parameters('date, 'salary) { (date, salary) =>
              get {
                complete {
                  (specificRequestsHandler ? GetContractByEmployeeBirthdayAndSalary(date.toLong, salary.toDouble)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("sixth") {
            parameters('employee, 'date) { (employee, date) =>
              get {
                complete {
                  (specificRequestsHandler ? GetSalesReportByEmployeeAndDate(employee, date.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("seventh") {
            parameters('salary, 'address) { (salary, address) =>
              get {
                complete {
                  (specificRequestsHandler ? GetPositionBySalaryAndCoffeeHouse(salary.toDouble, address)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("eighth") {
            parameters('firstDate, 'secondDate, 'contractDate) { (firstDate, secondDate, contractDate) =>
              get {
                complete {
                  (specificRequestsHandler ? GetEmployeeByBirthdayFirstAndBirthdaySecondAndContractDate(firstDate.toLong, secondDate.toLong, contractDate.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("nainth") {
            parameters('fromDate) { (fromDate) =>
              get {
                complete {
                  (specificRequestsHandler ? GetCoffeeHouseByMaxSalesReportsAndFromDate(fromDate.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          } ~ path("tenth") {
            parameters('address, 'fromDate) { (address, fromDate) =>
              get {
                complete {
                  (specificRequestsHandler ? GetEmployeeByCoffeeHouseAndMaxSalesReport(address, fromDate.toLong)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            }
          }
        }
      }

    }
}
