package com.polianskyi.csn

import com.polianskyi.csn.dao.CoffeeHouseDao
import com.polianskyi.csn.domain.CoffeeHouse
import org.slf4j.{Logger, LoggerFactory}

class Facade {

  val log: Logger = LoggerFactory.getLogger(classOf[Facade])

  def doThings(entity: CoffeeHouse): Unit = {

    log.info("into Facade")
    CoffeeHouseDao.create(entity)
  }

}
