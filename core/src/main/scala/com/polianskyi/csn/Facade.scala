package com.polianskyi.csn

import com.polianskyi.csn.dao.CoffeeHouseDao
import com.polianskyi.csn.domain.CoffeeHouse

class Facade {

  def doThings(entity: CoffeeHouse): Unit = {
    println("into Facade")
    CoffeeHouseDao.create(entity)
  }

}
