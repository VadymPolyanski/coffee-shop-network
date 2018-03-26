package com.polianskyi.csn

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor

object CsnHttpService extends App with CoffeeHouseHttpService {
  override implicit val system: ActorSystem = ActorSystem()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher
  override implicit val materializer: ActorMaterializer = ActorMaterializer()


  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)
  override def coffeeHouseHandler = system.actorOf(CoffeeHouseHandler.props())


  Http().bindAndHandle(routes , config.getString("http.interface"), config.getInt("http.port"))
}