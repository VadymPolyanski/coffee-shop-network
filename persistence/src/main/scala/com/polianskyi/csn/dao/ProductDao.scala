package com.polianskyi.csn.dao
import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain.Product
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object ProductDao extends GenericDao[Product, String] {

  private val allProductsForCD: String = "SELECT p.name, p.price, p.unit_of_measurement, p.description " +
    "FROM products as p INNER JOIN coffee_drinks_products ON p.name = coffee_drinks_products.coffee_drink_name" +
  "\n WHERE coffee_drinks_products.coffee_drink_name="

  override def findByPk(id: String): Future[Option[Product]] = ???

  def findAllByCoffeeDrink(coffeeDrinkName: String): Future[Option[List[Product]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(allProductsForCD  + s"'$coffeeDrinkName';")

      convertResultToList(rs,
        result => Product(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getString(4)))
    })
  }

  override def findAll(): Future[Option[List[Product]]] = ???

  override def delete(id: String): Future[Option[String]] = ???

  override def create(entity: Product): Future[Option[Product]] = ???

  override def update(entity: Product): Future[Option[Product]] = ???


}
