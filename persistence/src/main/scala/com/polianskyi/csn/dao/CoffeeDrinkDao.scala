package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.CoffeeDrink
import com.polianskyi.csn.domain.Product
import com.polianskyi.csn.system.PostgresConnector
import com.polianskyi.csn.ConverterUtil._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.Try

object CoffeeDrinkDao extends GenericDao[CoffeeDrink, String] {
  private val insert: String = "INSERT INTO coffee_drinks (name, price, native_price, description)" +
    "\nVALUES (?, ?, ?, ?);"

  private val insertWithProducts: String = "INSERT INTO coffee_drinks_products (coffee_drink_name, product_name)" +
    "\nVALUES (?, ?);"

  private val deleteProducts: String = "DELETE FROM coffee_drinks_products" +
    "\nWHERE coffee_drink_name="

  private val update: String = "UPDATE coffee_drinks SET name=?, price=?, native_price=?, description=?" +
    "\nWHERE name=?;"

  private val delete: String = "DELETE FROM coffee_drinks WHERE name=?;"

  private val selectByName: String = "SELECT name, price, native_price, description " +
    "FROM coffee_drinks WHERE name=?;"

  private val selectAll: String = "SELECT * " +
    "FROM coffee_drinks;"


  override def findByPk(name: String): Future[Option[CoffeeDrink]] = {
    PostgresConnector.withPreparedStatement(selectByName, pstmt => {
      pstmt.setString(1, name)

      val result = pstmt.executeQuery()

      if(result.next()) {
        var products: List[Product] = Nil
        ProductDao.findAllByCoffeeDrink(result.getString(1)).onComplete(prod => products = prod.get.get)

        Future.successful(Option(CoffeeDrink(result.getString(1), result.getDouble(2), result.getDouble(3), products, result.getString(4))))
      } else
        Future.successful(Option.empty)

    })
  }

  override def findAll(): Future[Option[List[CoffeeDrink]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAll)
      convertResultToList(rs,
        result => {
          var products: List[Product] = Nil
          ProductDao.findAllByCoffeeDrink(result.getString(1)).onComplete(prod => products = prod.get.get)

          CoffeeDrink(result.getString(1), result.getDouble(2), result.getDouble(3), products, result.getString(4))
        })
    })
  }

  override def delete(name: String): Future[Option[String]] = {
    PostgresConnector.withPreparedStatement(delete, pstmt => {
      pstmt.setString(1, name)
      if (pstmt.execute()){
        pstmt.getConnection.createStatement().execute(deleteProducts + s"'$name';")
        Future.successful(Option(name))
      } else
        Future.successful(Option.empty)
    })
  }

  override def create(entity: CoffeeDrink): Future[Option[CoffeeDrink]] =
    PostgresConnector.withPreparedStatement(insert, pstmt => {
      pstmt.setString(1, entity.name)
      pstmt.setDouble(2, entity.price)
      pstmt.setDouble(3, entity.nativePrice)
      pstmt.setString(4, entity.description)
      pstmt.execute()

      val productPstmt = pstmt.getConnection.prepareStatement(insertWithProducts)

      entity.products.foreach(product => {
        productPstmt.setString(1, entity.name)
        productPstmt.setString(2, product.name)
        productPstmt.execute()
        productPstmt.clearParameters()
      })

      Future.successful(Option(entity))
    })

  override def update(entity: CoffeeDrink): Future[Option[CoffeeDrink]] = {
    PostgresConnector.withPreparedStatement(update, pstmt => {
      pstmt.setString(1, entity.name)
      pstmt.setDouble(2, entity.price)
      pstmt.setDouble(3, entity.nativePrice)
      pstmt.setString(4, entity.description)
      pstmt.setString(5, entity.name)
      pstmt.execute()

      pstmt.getConnection.createStatement().execute(deleteProducts + s"'${entity.name}';")

      val productPstmt = pstmt.getConnection.prepareStatement(insertWithProducts)

      entity.products.foreach(product => {
        productPstmt.setString(1, entity.name)
        productPstmt.setString(2, product.name)
        productPstmt.execute()
        productPstmt.clearParameters()
      })

      Future.successful(Option(entity))
    })
  }
}
