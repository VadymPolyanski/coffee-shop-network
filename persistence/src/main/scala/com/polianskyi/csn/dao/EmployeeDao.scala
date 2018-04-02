package com.polianskyi.csn.dao

import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain.Employee
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object EmployeeDao extends GenericDao[Employee, String] {
  private val insert: String = "INSERT INTO employees (full_name, birthday_date, mobile_number, address, passport, sex)" +
    "\nVALUES (?, ?, ?, ?, ?, ?);"

  private val select: String = "SELECT full_name, birthday_date, mobile_number, address, passport, sex " +
    "FROM employees WHERE full_name=?;"

  private val selectAll: String = "SELECT * " +
    "FROM employees;"

  private val selectAllByCoffeeHouse: String = "SELECT * " +
    "FROM employees INNER JOIN contracts ON contracts.employee=employees.full_name" +
    "WHERE contracts.caffe_address=?;"

  override def findByPk(id: String): Future[Option[Employee]] =
    PostgresConnector.withPreparedStatement(select, pstmt => {
      pstmt.setString(1, id)

      val result = pstmt.executeQuery()

      if(result.next())
        Future.successful(Option(Employee(result.getString(1), result.getLong(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6))))
      else
        Future.successful(Option.empty)

    })

  override def findAll(): Future[Option[List[Employee]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAll)
      convertResultToList(rs,
        result => Employee(result.getString(1), result.getLong(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)))
    })
  }

  def findAllByCoffeeHouse(address: String): Future[Option[List[Employee]]] =  {
    PostgresConnector.withPreparedStatement(selectAllByCoffeeHouse, pstmt => {
      pstmt.setString(1, address)
      val rs = pstmt.executeQuery()

      convertResultToList(rs,
        result => Employee(result.getString(1), result.getLong(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)))
    })
  }

  override def delete(id: String): Future[Option[String]] = ???
  override def create(entity: Employee): Future[Option[Employee]] =
    PostgresConnector.withPreparedStatement(insert, pstmt => {
      pstmt.setString(1, entity.fullName)
      pstmt.setLong(2, entity.birthdayDate)
      pstmt.setString(3, entity.mobileNumber)
      pstmt.setString(4, entity.address)
      pstmt.setString(5, entity.passport)
      pstmt.setString(6, entity.sex)
      pstmt.execute()

      Future.successful(Option(entity))
    })

  override def update(entity: Employee): Future[Option[Employee]] = ???
}
