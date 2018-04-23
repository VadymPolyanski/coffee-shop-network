package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives.{complete, get, logRequestResult, path, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.polianskyi.csn.SpecificRequestsHandler._
import com.polianskyi.csn.domain.{FirstQueryAnswer, SecondQueryAnswer, ThirdQueryAnswer}
import com.polianskyi.csn.service.Protocols

trait SpecificRequestsHttpService extends Protocols with GenericHttpService {
  def specificRequestsHandler: ActorRef

  val specificRequestsRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("queries") {
            path("first") {
              parameters('productName, 'address) { (productName, address) =>
                get {
                  complete {
                    (specificRequestsHandler ? GetSuppliersByProductAndCoffeeHouse(productName, address)).map {
                      case list: List[FirstQueryAnswer] => Some(list)
                      case _ => Some(Nil)
                    }
                  }
                }
              }
            } ~ path("second") {
              parameters('address, 'position, 'startDate) { (address, position, startDate) =>
                get {
                  complete {
                    (specificRequestsHandler ? GetEmployeesByPositionAndCoffeeHouseAndDate(address, position, startDate.toLong)).map {
                      case list: List[SecondQueryAnswer] => Some(list)
                      case _ => Some(Nil)
                    }
                  }
                }
              }
            } ~ path("third") {
              parameters('address, 'saleDate) { (address, saleDate) =>
                get {
                  complete {
                    (specificRequestsHandler ? GetGoodsByProductsAndSalesReportAndCoffeeHouseAndDate(address, saleDate.toLong)).map {
                      case list: List[ThirdQueryAnswer] => Some(list)
                      case _ => Some(Nil)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
}
