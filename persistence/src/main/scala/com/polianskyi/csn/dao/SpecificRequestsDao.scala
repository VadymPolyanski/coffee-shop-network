package com.polianskyi.csn.dao

import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain._
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object SpecificRequestsDao {

  val getSuppliersByProductAndCoffeeHouseSql: String = "" +
    "SELECT full_name, company, product \n" +
    "FROM suppliers s\n" +
    "INNER JOIN products p ON p.name = s.product\n" +
    "INNER JOIN coffee_houses ch ON ch.address = s.coffee_house\n" +
    "WHERE ch.address = ? AND p.name = ?;"

  val getEmployeesByPositionAndCoffeeHouseAndDateSql: String = "" +
    "SELECT full_name, birthday_date, mobile_number, sex\n" +
    "FROM employees e\n" +
    "INNER JOIN contracts c ON c.employee = e.full_name\n" +
    "WHERE  c.coffee_house = ? AND c.position = ? AND c.start_date > ?;"


  val getGoodsByProductsAndSalesReportAndCoffeeHouseAndDateSql: String = "" +
    "SELECT coffee_drink, employee, cd.price, cd.description\n" +
    "FROM sales_reports sr\n" +
    "INNER JOIN coffee_drinks cd ON cd.name = sr.coffee_drink\n" +
    "WHERE sr.coffee_house = ? AND sr.sale_date > ?;"


  val getCoffeeHouseByProductAndMaxSalesReportsAndDateSql: String = "" +
    "SELECT ch.address, ch.space, ch.mobile_number, COUNT(*) AS sales\n" +
    "FROM sales_reports sr\n" +
    "INNER JOIN coffee_houses ch ON ch.address = sr.coffee_house\n" +
    "WHERE sr.coffee_drink = ? AND sr.sale_date > ?\n" +
    "GROUP BY ch.address\n" +
    "ORDER BY sales\n" +
    "LIMIT 1;"


  val getContractByEmployeeAgeAndSalarySql: String = "" +
    "SELECT contract_number, employee, position, start_date, hours_per_week, salary\n" +
    "FROM contracts c\n" +
    "INNER JOIN employees e ON e.full_name = c.employee\n" +
    "WHERE e.birthday_date > ? AND c.salary > ?;"


  val getSalesReportByEmployeeAndDateSql: String = "" +
    "SELECT coffee_drink, sale_date, price_with_vat\n" +
    "FROM sales_reports\n" +
    "WHERE employee = ? AND sale_date > ?;"


  val getPositionBySalaryAndCoffeeHouseSql: String = "" +
    "SELECT DISTINCT name, avg_salary\n" +
    "FROM positions p\n" +
    "INNER JOIN contracts c ON c.position = p.name\n" +
    "WHERE c.coffee_house = ? AND p.avg_salary > ?;"


  val getEmployeeByBirthdayFirstAndBirthdaySecondAndContractDateSql: String = "" +
    "SELECT full_name, birthday_date, mobile_number, sex\n" +
    "FROM employees e\n" +
    "INNER JOIN contracts c ON c.employee = e.full_name\n" +
    "WHERE e.birthday_date > ? AND e.birthday_date < ? AND c.end_date < ?;"


  val getCoffeeHouseByMaxSalesReportsAndFromDateSql: String = "" +
    "SELECT ch.address, ch.mobile_number, ch.space, SUM(sr.price_with_vat) sales\n" +
    "FROM coffee_houses ch\n" +
    "INNER JOIN sales_reports sr ON ch.address = sr.coffee_house\n" +
    "WHERE sr.sale_date > ?\n" +
    "GROUP BY ch.address;"


  val getEmployeeByCoffeeHouseAndMaxSalesReportSql: String = "" +
    "SELECT full_name, birthday_date, COUNT(sr.price_with_vat) as sales\n" +
    "FROM employees e\n" +
    "INNER JOIN sales_reports sr ON sr.employee = e.full_name\n" +
    "WHERE sr.sale_date > ? AND sr.coffee_house = ?\n" +
    "GROUP BY full_name\n" +
    "ORDER BY sales\n" +
    "LIMIT 1;"


  def getSuppliersByProductAndCoffeeHouse(productName: String, address: String): Future[Option[List[FirstQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getSuppliersByProductAndCoffeeHouseSql, pstmt => {
      pstmt.setString(1, address)
      pstmt.setString(2, productName)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => FirstQueryAnswer(result.getString(1), result.getString(2), result.getString(3)))
    })
  }

  def getEmployeesByPositionAndCoffeeHouseAndDate(address: String, position: String, date: Long): Future[Option[List[SecondQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getEmployeesByPositionAndCoffeeHouseAndDateSql, pstmt => {
      pstmt.setString(1, address)
      pstmt.setString(2, position)
      pstmt.setLong(3, date)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => SecondQueryAnswer(result.getString(1), result.getLong(2), result.getString(3), result.getString(4)))
    })
  }

  def getGoodsByProductsAndSalesReportAndCoffeeHouseAndDate(address: String, date: Long): Future[Option[List[ThirdQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getGoodsByProductsAndSalesReportAndCoffeeHouseAndDateSql, pstmt => {
      pstmt.setString(1, address)
      pstmt.setLong(2, date)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => ThirdQueryAnswer(result.getString(1), result.getString(2), result.getDouble(3), result.getString(4)))
    })
  }

  def getCoffeeHouseByProductAndMaxSalesReportsAndDate(coffeeDrinkName: String, date: Long): Future[Option[List[FourthQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getCoffeeHouseByProductAndMaxSalesReportsAndDateSql, pstmt => {
      pstmt.setString(1, coffeeDrinkName)
      pstmt.setLong(2, date)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => FourthQueryAnswer(result.getString(1), result.getDouble(2), result.getString(3), result.getInt(4)))
    })
  }

  def getContractByEmployeeAgeAndSalary(date: Long, salary: Double): Future[Option[List[FivethQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getContractByEmployeeAgeAndSalarySql, pstmt => {
      pstmt.setLong(1, date)
      pstmt.setDouble(2, salary)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => FivethQueryAnswer(result.getLong(1), result.getString(2), result.getString(3), result.getLong(4), result.getInt(5), result.getDouble(6)))
    })
  }

  def getSalesReportByEmployeeAndDate(employee: String, date: Long): Future[Option[List[SixthQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getSalesReportByEmployeeAndDateSql, pstmt => {
      pstmt.setString(1, employee)
      pstmt.setLong(2, date)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => SixthQueryAnswer(result.getString(1), result.getLong(2), result.getDouble(3)))
    })
  }

  def getPositionBySalaryAndCoffeeHouse(address: String, salary: Double): Future[Option[List[SeventhQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getPositionBySalaryAndCoffeeHouseSql, pstmt => {
      pstmt.setString(1, address)
      pstmt.setDouble(2, salary)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => SeventhQueryAnswer(result.getString(1), result.getDouble(2)))
    })
  }

  def getEmployeeByBirthdayFirstAndBirthdaySecondAndContractDate(firstDate: Long, secondDate: Long, contractDate: Long): Future[Option[List[EightthQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getEmployeeByBirthdayFirstAndBirthdaySecondAndContractDateSql, pstmt => {
      pstmt.setLong(1, firstDate)
      pstmt.setLong(2, secondDate)
      pstmt.setLong(3, contractDate)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => EightthQueryAnswer(result.getString(1), result.getLong(2), result.getString(3), result.getString(4)))
    })
  }

  def getCoffeeHouseByMaxSalesReportsAndFromDate(fromDate: Long): Future[Option[List[NainthQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getCoffeeHouseByMaxSalesReportsAndFromDateSql, pstmt => {
      pstmt.setLong(1, fromDate)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => NainthQueryAnswer(result.getString(1), result.getString(2), result.getDouble(3), result.getDouble(4)))
    })
  }

  def getEmployeeByCoffeeHouseAndMaxSalesReport(address: String, fromDate: Long): Future[Option[List[TenthQueryAnswer]]] = {
    PostgresConnector.withPreparedStatement(getEmployeeByCoffeeHouseAndMaxSalesReportSql, pstmt => {
      pstmt.setLong(1, fromDate)
      pstmt.setString(1, address)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => TenthQueryAnswer(result.getString(1), result.getLong(2), result.getInt(3)))
    })
  }
}
