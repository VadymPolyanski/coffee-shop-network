package com.polianskyi.csn

import akka.actor.{ActorRef, ActorSystem}
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.Materializer
import akka.util.Timeout
import com.polianskyi.csn.CoffeeHouseHandler._
import com.polianskyi.csn.domain.CoffeeHouse
import com.typesafe.config.Config
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.concurrent.ExecutionContextExecutor


case class CoffeeHousePwd(pwd:String)

trait Protocols extends DefaultJsonProtocol {
  implicit val deleteChFormat: RootJsonFormat[CoffeeHouseDeleted] = jsonFormat1(CoffeeHouseDeleted.apply)
  implicit val chNotFoundFormat: RootJsonFormat[CoffeeHouseNotFound] = jsonFormat1(CoffeeHouseNotFound.apply)
  implicit val coffeeHouseFormat: RootJsonFormat[CoffeeHouse] = jsonFormat4(CoffeeHouse.apply)
}

trait CoffeeHouseHttpService extends Protocols {

  import scala.concurrent.duration._

  implicit def executor: ExecutionContextExecutor
  implicit def requestTimeout: Timeout = Timeout(50 seconds)

  implicit val system: ActorSystem
  implicit val materializer: Materializer

  def config: Config
  def coffeeHouseHandler: ActorRef
  val logger: LoggingAdapter


  val routes: Route =
    logRequestResult("coffee-shop-network") {
      pathPrefix("coffe-houses") {
        path("create") {
          post {
            entity(as[CoffeeHouse]) { ch =>
              complete {
                (coffeeHouseHandler ? Create(ch.address, ch.space, ch.rentalPrice, ch.mobileNumber)).mapTo[CoffeeHouse]
              }
            }
          }
        } ~ path("all") {
          get {
            complete {
              (coffeeHouseHandler ? GetAllCoffeeHouses()).map {
                case list: List[CoffeeHouse] =>  Some(list)
                case Nil => Some(Nil)
              }
            }
          }
        } ~
          path(Segment) { address =>
            get {
              complete {
                (coffeeHouseHandler ? GetCoffeeHouse(address)).map {
                  case CoffeeHouse(a, s, p, m) => Some(CoffeeHouse(a, s, p, m))
                  case _ => None
                }
              }
            }
          } ~
          path(Segment) { address =>
            put {
              entity(as[CoffeeHouse]) { ch =>
                complete {
                  (coffeeHouseHandler ? Update(ch.address, ch.space, ch.rentalPrice, ch.mobileNumber)).map {
                    case false => InternalServerError -> s"Could not update coffee house with address $address"
                    case _ => NoContent -> ""
                  }
                }
              }
            }
          } ~
          path(Segment) { address =>
            delete {
              complete {
                (coffeeHouseHandler ? DeleteCoffeeHouse(address)).map {
                  case CoffeeHouseNotFound(`address`) => InternalServerError -> s"Could not delete coffee house with address $address"
                  case _: CoffeeHouseDeleted => NoContent -> ""
                }
              }
            }
          }
      }
    }
}