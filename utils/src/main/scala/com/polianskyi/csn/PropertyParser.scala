package com.polianskyi.csn

import scala.io.Source.fromResource

object PropertyParser {

  private val dbPropertiesFile                = "db.properties"
  private val properties: Map[String, String] = parseProperties(dbPropertiesFile)

  private def parseProperties(filename: String): Map[String, String] = {
    val lines      = fromResource(filename).getLines.toSeq
    val cleanLines = lines.map(_.trim).filter(!_.startsWith("#")).filter(_.contains("="))
    cleanLines.map(line => { val Array(a, b) = line.split("=", 2); (a.trim, b.trim) }).toMap
  }

  def getProperty(name: String): String =
    properties(name)
}
