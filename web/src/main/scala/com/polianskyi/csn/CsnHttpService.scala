package com.polianskyi.csn

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.polianskyi.csn.http._
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor
import scala.util.control.NonFatal

object CsnHttpService extends App with CoffeeHouseHttpService with CoffeeDrinkHttpService
  with EmployeeHttpService with ProductHttpService with  ContractHttpService with SalesReportHttpService
  with SpecificRequestsHttpService  with PositionHttpService {
  import scala.concurrent.duration._

  override implicit def requestTimeout: Timeout = Timeout(50 seconds)

  override implicit val system: ActorSystem = ActorSystem()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher
  override implicit val materializer: ActorMaterializer = ActorMaterializer()


  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)
  override def coffeeHouseHandler = system.actorOf(CoffeeHouseHandler.props())
  override def coffeeDrinkHandler = system.actorOf(CoffeeDrinkHandler.props())
  override def productHandler = system.actorOf(ProductHandler.props())
  override def contractHandler = system.actorOf(ContractHandler.props())
  override def employeeHandler = system.actorOf(EmployeeHandler.props())
  override def salesReportHandler = system.actorOf(SalesReportHandler.props())
  override def specificRequestsHandler = system.actorOf(SpecificRequestsHandler.props())
  override def positionHandler = system.actorOf(PositionHandler.props())


  Http().bindAndHandle(coffeeHouseRoutes ~ coffeeDrinkRoutes ~ productRoutes ~ contractRoutes ~ employeeRoutes ~ salesReportRoutes
    ~ specificRequestsRoutes ~ positionRoutes,
    config.getString("http.interface"), config.getInt("http.port"))

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case NonFatal(e) =>
        logger.error(s"Exception $e at\n${e.getStackTraceString}")
        complete(HttpResponse(InternalServerError, entity = "Internal Server Error"))
    }
}