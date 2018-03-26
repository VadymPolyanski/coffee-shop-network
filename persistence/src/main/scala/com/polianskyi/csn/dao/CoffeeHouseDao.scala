package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.CoffeeHouse
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object CoffeeHouseDao extends GenericDao[CoffeeHouse, String] {

  private val insert: String = "INSERT INTO coffee_houses (address, space, rental_price, mobile_number)" +
    "\nVALUES (?, ?, ?, ?);"

  override def findByPk(address: String): Future[Option[CoffeeHouse]] = ???

  override def findAll(): Future[List[CoffeeHouse]] = ???

  override def delete(address: String): Future[Option[CoffeeHouse]] = ???

  override def create(entity: CoffeeHouse): Future[Option[CoffeeHouse]] =
    PostgresConnector.withPreparedStatement(insert, stmt => {
      stmt.setString(1, entity.address)
      stmt.setDouble(2, entity.space)
      stmt.setDouble(3, entity.rentalPrice)
      stmt.setString(4, entity.mobileNumber)
      stmt.execute()

      entity
    })

  override def update(entity: CoffeeHouse): Future[Option[CoffeeHouse]] = ???
}
