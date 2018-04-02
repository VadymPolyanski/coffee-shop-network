package com.polianskyi.csn.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NoContent}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.polianskyi.csn.EmployeeHandler._
import com.polianskyi.csn.domain.Employee
import com.polianskyi.csn.service.Protocols


trait EmployeeHttpService extends Protocols with GenericHttpService{
  def employeeHandler: ActorRef

  val employeeRoutes: Route =
    cors(corsSettings) {
      logRequestResult("coffee-shop-network") {
        pathPrefix("api") {
          pathPrefix("employees") {
            path("create") {
              post {
                entity(as[Employee]) { ch =>
                  complete {
                    (employeeHandler ? Create(ch.fullName, ch.birthdayDate, ch.mobileNumber, ch.address, ch.passport, ch.sex)).mapTo[Employee]
                  }
                }
              }
            } ~ path("all") {
              get {
                complete {
                  (employeeHandler ? GetAllEmployees()).map {
                    case list: List[Employee] => Some(list)
                    case Nil => Some(Nil)
                  }
                }
              }
            } ~ path("in-office") {
              path(Segment) { address =>
                get {
                  complete {
                    (employeeHandler ? GetAllEmployeesByCoffeeHouse(address)).map {
                      case list: List[Employee] => Some(list)
                      case Nil => Some(Nil)
                    }
                  }
                }
              }
            } ~
              path(Segment) { fullName =>
                get {
                  complete {
                    (employeeHandler ? GetEmployee(fullName)).map {
                      case Employee(fn, bd, mn, a, p, s) => Some(Employee(fn, bd, mn, a, p, s))
                      case _ => None
                    }
                  }
                }
              } ~
              path(Segment) { fullName =>
                put {
                  entity(as[Employee]) { ch =>
                    complete {
                      (employeeHandler ? Update(ch.fullName, ch.birthdayDate, ch.mobileNumber, ch.address, ch.passport, ch.sex)).map {
                        case false => InternalServerError -> s"Could not update employee with fullName $fullName"
                        case _ => NoContent -> ""
                      }
                    }
                  }
                }
              } ~
              path(Segment) { fullName =>
                delete {
                  complete {
                    (employeeHandler ? DeleteEmployee(fullName)).map {
                      case EmployeeNotFound(`fullName`) => InternalServerError -> s"Could not delete employee with fullName $fullName"
                      case _: EmployeeDeleted => NoContent -> ""
                    }
                  }
                }
              }
          }
        }
      }
    }
}