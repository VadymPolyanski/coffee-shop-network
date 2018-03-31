package com.polianskyi.csn.system

import java.sql.{Connection, DriverManager, PreparedStatement, SQLException, Statement}

import com.polianskyi.csn.PropertyParser._
import org.slf4j.{Logger, LoggerFactory}

trait Connector {
  def withStatement[T](act: Statement => T): T
  def withPreparedStatement[T](query: String, act: PreparedStatement => T): T
}

object PostgresConnector extends Connector {

  val log: Logger = LoggerFactory.getLogger(PostgresConnector.getClass)


  private val user     = getProperty("jdbc.user")
  private val password = getProperty("jdbc.password")
  private val dburl    = getProperty("jdbc.url")
  private val dbname   = getProperty("jdbc.dbname")

  private val url: String = dburl + dbname

  {
    log.info("Started initialization of connector - creating db and tables if they don't exist")
    createDB()

    new DatabaseMigrationManager(
      dburl + dbname,
      user,
      password
    ).migrateDatabaseSchema()

    log.info("Finished initialization of connector")
  }

  override def withStatement[T](act: Statement => T): T = {
    log.info("Execute action with statement")
    getConnectionWithAction(url, conn => act(conn.createStatement()))
  }

  override def withPreparedStatement[T](query: String, act: PreparedStatement => T): T = {
    log.info("Execute action with prepared statement")
    getConnectionWithAction(url, conn => act(conn.prepareStatement(query)))
  }

  private def getConnectionWithAction[T](localUrl: String, act: Connection => T): T = {
    var connection: Connection = null
    try {
      log.info("Finished initialization of connector")
      connection = DriverManager.getConnection(localUrl, user, password)
      act(connection)
    } catch {
      case e: SQLException =>
        log.error("Exception when tried to get connection to {} as {} and execute sql. Sql state: {}", localUrl, user, e.getSQLState)
        e.printStackTrace()
        throw e
    }
    finally {
      if (connection != null)
        connection.close()
    }
  }

  def createDB(): Unit = {
    val sqlCheck = "SELECT 1 as check from pg_database WHERE datname='csn'"
    val sql = "CREATE DATABASE csn\n WITH OWNER = csn;"
    val localUrl = dburl + "postgres"


    if (!getConnectionWithAction(localUrl, conn => conn.createStatement().executeQuery(sqlCheck).next())) {
      log.info("DB csn doesn't exist. Creating new from user csn.")
      getConnectionWithAction(localUrl, conn => conn.createStatement().execute(sql))
    }
  }
}
