package com.polianskyi.csn

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.polianskyi.csn.http.{CoffeeDrinkHttpService, CoffeeHouseHttpService}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor

object CsnHttpService extends App with CoffeeHouseHttpService with CoffeeDrinkHttpService{
  import scala.concurrent.duration._

  override implicit def requestTimeout: Timeout = Timeout(50 seconds)

  override implicit val system: ActorSystem = ActorSystem()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher
  override implicit val materializer: ActorMaterializer = ActorMaterializer()


  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)
  override def coffeeHouseHandler = system.actorOf(CoffeeHouseHandler.props())
  override def coffeeDrinkHandler = system.actorOf(CoffeeDrinkHandler.props())


  Http().bindAndHandle(coffeeHouseRoutes ~ coffeeDrinkRoutes, config.getString("http.interface"), config.getInt("http.port"))
}