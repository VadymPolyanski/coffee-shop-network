package com.polianskyi.csn.system

import java.sql.{Connection, DriverManager, SQLException, Statement}

import com.polianskyi.csn.PropertyParser._

trait Connector {
  def useConnection[T](act: Statement => T): T
}

class PostgresConnector extends Connector {

  private val url: String = getProperty("jdbc.url") +
    "?user=" + getProperty("jdbc.user")
    "&password=" + getProperty("jdbc.password") +
    "&ssl=" + getProperty("jdbc.ssl")

  override def useConnection[T](act: Statement => T): T = {
    var connection:Connection = null
    try {
      connection = DriverManager.getConnection(url)

      act(connection.createStatement())
    } catch {
      case e: SQLException => throw e
    } finally {
      connection.close()
    }
  }
}
