package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.CoffeeHouse

class CoffeeHouseDao extends GenericDao[CoffeeHouse, String] {
  override def findById(id: String): CoffeeHouse = ???

  override def findAll(): List[CoffeeHouse] = ???

  override def delete(id: String): CoffeeHouse = ???

  override def create(entity: CoffeeHouse): CoffeeHouse = ???

  override def update(entity: CoffeeHouse): CoffeeHouse = ???
}
