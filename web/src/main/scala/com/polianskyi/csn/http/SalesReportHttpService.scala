package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.polianskyi.csn.SalesReportHandler._
import com.polianskyi.csn.domain.{SalesReport, SalesReportPK}
import com.polianskyi.csn.service.Protocols


trait SalesReportHttpService extends Protocols with GenericHttpService{
  def salesReportHandler: ActorRef
  val salesReportRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("salesReports") {
            path("create") {
              post {
                entity(as[SalesReport]) { sr =>
                  complete {
                    (salesReportHandler ? Create(sr.coffeeDrink, sr.employee, sr.priceWithVat, sr.saleDate, sr.coffeeHouse)).mapTo[SalesReport]
                  }
                }
              }
            } ~ path("all") {
              get {
                complete {
                  (salesReportHandler ? GetAllSalesReports()).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            } ~ path(Segment) { address =>
              get {
                complete {
                  (salesReportHandler ? GetSalesReportsByCoffeeHouse(address)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            } ~ path(Segment) { fullName =>
              get {
                complete {
                  (salesReportHandler ? GetSalesReportsByEmployee(fullName)).map {
                    case list: List[SalesReport] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            } ~ path(Segment) { _ =>
              get {
                entity(as[SalesReportPK]) { pk =>
                  complete {
                    (salesReportHandler ? GetSalesReport(pk)).map {
                      case SalesReport(d, e, p, s, c) => Some(SalesReport(d, e, p, s, c))
                      case _ => None
                    }
                  }
                }
              }
            } ~
              path(Segment) { name =>
                put {
                  entity(as[SalesReport]) { sr =>
                    complete {
                      (salesReportHandler ? Update(sr.coffeeDrink, sr.employee, sr.priceWithVat, sr.saleDate, sr.coffeeHouse)).map {
                        case false => InternalServerError -> s"Could not update salesReport with name $name"
                        case _ => NoContent -> ""
                      }
                    }
                  }
                }
              } ~
              path(Segment) { name =>
                delete {
                  entity(as[SalesReportPK]) { pk =>
                    complete {
                      (salesReportHandler ? DeleteSalesReport(pk)).map {
                        case SalesReportNotFound(`pk`) => InternalServerError -> s"Could not delete salesReport with name $name"
                        case _: SalesReportDeleted => NoContent -> ""
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