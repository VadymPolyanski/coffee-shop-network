package com.polianskyi.csn

import com.polianskyi.csn.domain.CoffeeHouse

class WebController {

  def runM(): Unit = {
    println("into Web")

    new Facade().doThings(CoffeeHouse("Bratslavska 104A", 106.3, 5200, "0984233"))

  }

}

object Main {
  def main(args: Array[String]): Unit =
    new WebController().runM()
}
