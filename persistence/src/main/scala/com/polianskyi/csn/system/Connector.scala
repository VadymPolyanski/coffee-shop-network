package com.polianskyi.csn.system

import java.sql.{ Connection, DriverManager, PreparedStatement, SQLException, Statement }
import com.polianskyi.csn.PropertyParser._

trait Connector {
  def withStatement[T](act: Statement => T): T
  def withPreparedStatement[T](query: String, act: PreparedStatement => T): T
}

object PostgresConnector extends Connector {

  private val user     = getProperty("jdbc.user")
  private val password = getProperty("jdbc.password")
  private val dburl    = getProperty("jdbc.url")
  private val dbname   = getProperty("jdbc.dbname")

  private val url: String = dburl + dbname

  //initialization - creating db and tables if they don't exist
  {
    createDB()

    new DatabaseMigrationManager(
      dburl + dbname,
      user,
      password
    ).migrateDatabaseSchema()
  }

  override def withStatement[T](act: Statement => T): T =
    getConnectionWithAction(url, conn => act(conn.createStatement()))

  override def withPreparedStatement[T](query: String, act: PreparedStatement => T): T =
    getConnectionWithAction(url, conn => act(conn.prepareStatement(query)))

  private def getConnectionWithAction[T](localUrl: String, act: Connection => T): T = {
    var connection: Connection = null
    try {
      connection = DriverManager.getConnection(localUrl, user, password)
      act(connection)
    } catch {
      case e: SQLException => throw e
    }
    finally {
      if (connection != null)
        connection.close()
    }
  }

  def createDB(): Unit = {
    val sqlCheck = "SELECT 1 as check from pg_database WHERE datname='csn'"
    val sql = "CREATE DATABASE csn\n WITH OWNER = csn;"

    if (!getConnectionWithAction(dburl + "postgres", conn => conn.createStatement().executeQuery(sqlCheck)next())) {
      getConnectionWithAction(dburl + "postgres", conn => conn.createStatement().execute(sql))
    }
  }
}
