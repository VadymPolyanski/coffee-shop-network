package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.CoffeeHouse
import com.polianskyi.csn.system.PostgresConnector

object CoffeeHouseDao extends GenericDao[CoffeeHouse, String] {

  private val insert: String = "INSERT INTO coffee_house (address, space, rental_price, mobile_number)" +
    "\nVALUES (?, ?, ?, ?);"

  override def findById(id: String): CoffeeHouse = ???

  override def findAll(): List[CoffeeHouse] = ???

  override def delete(id: String): CoffeeHouse = ???

  override def create(entity: CoffeeHouse): CoffeeHouse =
    PostgresConnector.withPreparedStatement(insert, stmt => {
      stmt.setString(0, entity.address)
      stmt.setDouble(1, entity.space)
      stmt.setDouble(2, entity.rentalPrice)
      stmt.setString(3, entity.mobileNumber)
      stmt.execute()

      entity
    })

  override def update(entity: CoffeeHouse): CoffeeHouse = ???
}
