package com.polianskyi.csn.http

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, POST, PUT}
import akka.stream.Materializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.config.Config

import scala.concurrent.ExecutionContextExecutor

trait GenericHttpService {


  import scala.concurrent.duration._

  implicit def executor: ExecutionContextExecutor
  implicit def requestTimeout: Timeout = Timeout(50 seconds)

  implicit val system: ActorSystem
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  val corsSettings: CorsSettings = CorsSettings.defaultSettings.withAllowGenericHttpRequests(true).withAllowCredentials(true)
    .withAllowedMethods(List(GET, PUT, POST, DELETE))
}
