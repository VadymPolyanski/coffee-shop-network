package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.polianskyi.csn.CoffeeDrinkHandler._
import com.polianskyi.csn.domain.CoffeeDrink
import com.polianskyi.csn.service.Protocols

trait CoffeeDrinkHttpService extends Protocols with GenericHttpService{
  def coffeeDrinkHandler: ActorRef

  val coffeeDrinkRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("coffee-drinks") {
            path("create") {
              post {
                entity(as[CoffeeDrink]) { ch =>
                  complete {
                    (coffeeDrinkHandler ? Create(ch.name, ch.price, ch.nativePrice, ch.products, ch.description)).mapTo[CoffeeDrink]
                  }
                }
              }
            } ~ path("all") {
              get {
                complete {
                  (coffeeDrinkHandler ? GetAllCoffeeDrinks()).map {
                    case list: List[CoffeeDrink] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            } ~
              path(Segment) { name =>
                get {
                  complete {
                    (coffeeDrinkHandler ? GetCoffeeDrink(name)).map {
                      case CoffeeDrink(n, p, np, pr, d) => Some(CoffeeDrink(n, p, np, pr, d))
                      case _ => None
                    }
                  }
                }
              } ~
              path(Segment) { name =>
                put {
                  entity(as[CoffeeDrink]) { ch =>
                    complete {
                      (coffeeDrinkHandler ? Update(ch.name, ch.price, ch.nativePrice, ch.products, ch.description)).map {
                        case false => InternalServerError -> s"Could not update coffee drink with name $name"
                        case _ => NoContent -> ""
                      }
                    }
                  }
                }
              } ~
              path(Segment) { name =>
                delete {
                  complete {
                    (coffeeDrinkHandler ? DeleteCoffeeDrink(name)).map {
                      case CoffeeDrinkNotFound(`name`) => InternalServerError -> s"Could not delete coffee drink with name $name"
                      case _: CoffeeDrinkDeleted => NoContent -> ""
                    }
                  }
                }
              }
          }
        }
      }
    }
}