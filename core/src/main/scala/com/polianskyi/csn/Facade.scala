package com.polianskyi.csn

class Facade {

  def doThings(entity: Entity): Unit = {
    println("into Facade")

    FacadeUtil.printEnt(entity.id, entity.nested.value)
  }

}
