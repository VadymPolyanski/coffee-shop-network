package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives.{complete, get, logRequestResult, path, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.polianskyi.csn.PositionHandler.GetAllPositions
import com.polianskyi.csn.domain.Position
import com.polianskyi.csn.service.Protocols

trait PositionHttpService extends Protocols with GenericHttpService {
  def positionHandler: ActorRef

  val positionRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("positions") {
            path("all") {
              get {
                complete {
                  (positionHandler ? GetAllPositions()).map {
                    case list: List[Position] => Some(list)
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