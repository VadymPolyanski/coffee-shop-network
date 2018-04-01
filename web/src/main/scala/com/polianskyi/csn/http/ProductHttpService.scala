package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.polianskyi.csn.ProductHandler._
import com.polianskyi.csn.domain.Product
import com.polianskyi.csn.service.Protocols


trait ProductHttpService extends Protocols with GenericHttpService{
  def productHandler: ActorRef

  val productRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("products") {
          path("create") {
            post {
              entity(as[Product]) { ch =>
                complete {
                  (productHandler ? Create(ch.name, ch.price, ch.unitOfMeasurement, ch.description)).mapTo[Product]
                }
              }
            }
          } ~ path("all") {
            get {
              complete {
                (productHandler ? GetAllProducts()).map {
                  case list: List[Product] => Some(list)
                  case Nil => Some(Nil)
                }
              }
            }
          } ~
            path(Segment) { name =>
              get {
                complete {
                  (productHandler ? GetProduct(name)).map {
                    case Product(n, p, u, d) => Some(Product(n, p, u, d))
                    case _ => None
                  }
                }
              }
            } ~
            path(Segment) { name =>
              put {
                entity(as[Product]) { ch =>
                  complete {
                    (productHandler ? Update(ch.name, ch.price, ch.unitOfMeasurement, ch.description)).map {
                      case false => InternalServerError -> s"Could not update product with name $name"
                      case _ => NoContent -> ""
                    }
                  }
                }
              }
            } ~
            path(Segment) { name =>
              delete {
                complete {
                  (productHandler ? DeleteProduct(name)).map {
                    case ProductNotFound(`name`) => InternalServerError -> s"Could not delete product with name $name"
                    case _: ProductDeleted => NoContent -> ""
                  }
                }
              }
            }
        }
      }
    }
}