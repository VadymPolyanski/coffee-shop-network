package com.polianskyi.csn

import java.sql.ResultSet

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

object ConverterUtil {
  def convertResultToList[T](rs: ResultSet, act: ResultSet => T): Future[Option[List[T]]] = {
    val temporaryBuffer = ListBuffer.empty[T]
    if (rs.next()) {
      temporaryBuffer += act(rs)
      while ( {rs.next}) {
        temporaryBuffer += act(rs)
      }
      Future.successful(Option(temporaryBuffer.toList))
    } else
      Future.successful(Option(Nil))
  }
}
