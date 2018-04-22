package com.polianskyi.csn.system

import org.flywaydb.core.Flyway

class DatabaseMigrationManager(jdbcUrl: String, dbUser: String, dbPassword: String) {

  private val flyway = new Flyway()

  flyway.setDataSource(jdbcUrl, dbUser, dbPassword)
  flyway.setBaselineOnMigrate(true)

  def migrateDatabaseSchema(): Unit = flyway.migrate()

  def dropDatabase(): Unit = flyway.clean()
}
