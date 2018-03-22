package com.polianskyi.csn

class WebController {

  def runM(): Unit = {
    println("into Web")

    new Facade().doThings(Entity("the best", NestedEntity("It's WIN")))

  }

}

object Main {
  def main(args: Array[String]): Unit =
    new WebController().runM()
}
