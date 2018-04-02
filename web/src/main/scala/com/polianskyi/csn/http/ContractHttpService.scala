package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.polianskyi.csn.ContractHandler._
import com.polianskyi.csn.domain.Contract
import com.polianskyi.csn.service.Protocols


trait ContractHttpService extends Protocols with GenericHttpService{
  def contractHandler: ActorRef

  val contractRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("contracts") {
            path("create") {
              post {
                entity(as[Contract]) { ch =>
                  complete {
                    (contractHandler ? Create(ch.contractNumber,ch.workPosition,ch.startDate,ch.endDate,ch.hoursPerWeek,ch.employee,ch.coffeeHouse,ch.salary,ch.vacation)).mapTo[Contract]
                  }
                }
              }
            } ~ path("all") {
              get {
                complete {
                  (contractHandler ? GetAllContracts()).map {
                    case list: List[Contract] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            } ~
              path(Segment) { contractNumber =>
                get {
                  complete {
                    (contractHandler ? GetContract(contractNumber.toInt)).map {
                      case Contract(cn, w, s, e, h, em,ch, sa, v) => Some(Contract(cn, w, s, e, h, em,ch, sa, v))
                      case _ => None
                    }
                  }
                }
              } ~
              path(Segment) { contractNumber =>
                put {
                  entity(as[Contract]) { ch =>
                    complete {
                      (contractHandler ? Update(ch.contractNumber,ch.workPosition,ch.startDate,ch.endDate,ch.hoursPerWeek,ch.employee,ch.coffeeHouse,ch.salary,ch.vacation)).map {
                        case false => InternalServerError -> s"Could not update contract with contractNumber $contractNumber"
                        case _ => NoContent -> ""
                      }
                    }
                  }
                }
              } ~
              path(Segment) { contractNumber =>
                delete {
                  complete {
                    (contractHandler ? DeleteContract(contractNumber.toInt)).map {
                      case ContractNotFound(`contractNumber`.toInt) => InternalServerError -> s"Could not delete contract with contractNumber $contractNumber"
                      case _: ContractDeleted => NoContent -> ""
                    }
                  }
                }
              }
          }
        }
      }
    }
}