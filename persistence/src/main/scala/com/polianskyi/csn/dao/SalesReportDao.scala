package com.polianskyi.csn.dao

import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain._
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object SalesReportDao extends GenericDao[SalesReport, SalesReportPK] {


  private val selectALL: String = "SELECT sr.coffee_drink, sr.employee, sr.price_with_vat, sr.sale_date, sr.caffee_address," + //1-5
    "e.full_name, e.birthday_date, e.mobile_number, e.address, e.passport, e.sex " + //6-11
    "cd.name, cd.price, cd.native_price, cd.description " + //12-15
    "ch.address, ch.space, ch.rental_price, ch.mobile_number" + //13-16
    "FROM sales_reports sr " +
    "INER JOIN employees e ON e.full_name=sr.employee " +
    "INER JOIN coffee_drinks cd ON cd.name=sr.coffee_drink " +
    "INER JOIN coffee_houses ch ON ch.address=sr.caffee_address "

  private val selectByCoffeeHouse: String = selectALL +
    "WHERE sr.caffee_address=?;"

  private val selectByEmployee: String = selectALL +
    "WHERE sr.employee=?;"


  override def findByPk(id: SalesReportPK): Future[Option[SalesReport]] = ???

  override def findAll(): Future[Option[List[SalesReport]]] = ???

  def findAllByCoffeeHouse(address: String): Future[Option[List[SalesReport]]] =
    PostgresConnector.withPreparedStatement(selectByCoffeeHouse, pstmt => {
      pstmt.setString(1, address)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => {
          val employee: Employee = Employee(result.getString(6), result.getLong(7), result.getString(8), result.getString(9), result.getString(10), result.getString(11))
          val coffeeDrink: CoffeeDrink = CoffeeDrink(result.getString(12), result.getDouble(13), result.getDouble(14), Nil, result.getString(15))
          val coffeeHouse: CoffeeHouse = CoffeeHouse(result.getString(13), result.getDouble(14), result.getDouble(15), result.getString(16))

          SalesReport(coffeeDrink, employee, result.getDouble(3), result.getLong(4), coffeeHouse)
        })
    })

  def findAllByEmployee(fullName: String): Future[Option[List[SalesReport]]] =
    PostgresConnector.withPreparedStatement(selectByEmployee, pstmt => {
      pstmt.setString(1, fullName)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => {
          val employee: Employee = Employee(result.getString(6), result.getLong(7), result.getString(8), result.getString(9), result.getString(10), result.getString(11))
          val coffeeDrink: CoffeeDrink = CoffeeDrink(result.getString(12), result.getDouble(13), result.getDouble(14), Nil, result.getString(15))
          val coffeeHouse: CoffeeHouse = CoffeeHouse(result.getString(13), result.getDouble(14), result.getDouble(15), result.getString(16))

          SalesReport(coffeeDrink, employee, result.getDouble(3), result.getLong(4), coffeeHouse)
        })
    })

  override def delete(id: SalesReportPK): Future[Option[SalesReportPK]] = ???

  override def create(entity: SalesReport): Future[Option[SalesReport]] = ???

  override def update(entity: SalesReport): Future[Option[SalesReport]] = ???
}
