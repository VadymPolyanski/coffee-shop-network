package com.polianskyi.csn.dao
import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain.Product
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object ProductDao extends GenericDao[Product, String] {

  private val allProductsForCD: String = "SELECT p.name, p.price, p.unit_of_measurement, p.description " +
    "FROM products as p INNER JOIN coffee_drinks_products ON p.name = coffee_drinks_products.coffee_drink_name" +
  "\n WHERE coffee_drinks_products.coffee_drink_name="

  private val selectAll: String = "SELECT * " +
    "FROM products;"

  private val insert: String = "INSERT INTO public.products(name, price, unit_of_measurement, description)\n" +
    "VALUES (?, ?, ?, ?);"

  def findAllByCoffeeDrink(coffeeDrinkName: String): Future[Option[List[Product]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(allProductsForCD  + s"'$coffeeDrinkName';")

      convertResultToList(rs,
        result => Product(result.getString(1), result.getDouble(2), result.getString(3), result.getString(4)))
    })
  }

  override def findByPk(id: String): Future[Option[Product]] = ???

  override def findAll(): Future[Option[List[Product]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAll)
      convertResultToList(rs,
        result => Product(result.getString(1), result.getDouble(2), result.getString(3), result.getString(4)))
    })
  }

  override def delete(id: String): Future[Option[String]] = ???

  override def create(entity: Product): Future[Option[Product]] =
    PostgresConnector.withPreparedStatement(insert, pstmt => {
      pstmt.setString(1, entity.name)
      pstmt.setDouble(2, entity.price)
      pstmt.setString(3, entity.unitOfMeasurement)
      pstmt.setString(4, entity.description)
      pstmt.execute()

      Future.successful(Option(entity))
    })

  override def update(entity: Product): Future[Option[Product]] = ???


}
