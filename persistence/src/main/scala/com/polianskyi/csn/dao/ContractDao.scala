package com.polianskyi.csn.dao

import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain.{CoffeeHouse, Contract, Employee}
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ContractDao extends GenericDao[Contract, Int]{
  private val insert: String = "INSERT INTO contracts (contract_number, work_position, start_date, end_date, hours_per_week, employee, caffe_address, salary, vacation)" +
    "\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"

  private val update: String = "UPDATE contracts SET contract_number=?, work_position=?, start_date=?, end_date=?, hours_per_week=?, employee=?, caffe_address=?, salary=?, vacation=?" +
    "\nWHERE contract_number=?;"

  private val delete: String = "DELETE FROM contracts WHERE contract_number=?;"

  private val selectAll: String = "SELECT * " +
    "FROM contracts;"

  private val selectByContractNumber: String = "SELECT contract_number, work_position, start_date, end_date, hours_per_week, employee, caffe_address, salary, vacation " +
    "FROM contracts WHERE contract_number=?;"

  private val selectByEmployee: String = "SELECT contract_number, work_position, start_date, end_date, hours_per_week, employee, caffe_address, salary, vacation " +
    "FROM contracts WHERE employee=?;"

  def findByEmployee(employee: String): Future[Option[Contract]] =
    PostgresConnector.withPreparedStatement(selectByEmployee, pstmt => {
      pstmt.setString(1, employee)

      val result = pstmt.executeQuery()

      if(result.next())
        Future.successful(Option(Contract(result.getInt(1), result.getString(2), result.getLong(3), result.getLong(4), result.getInt(5), null, null, result.getDouble(8), result.getInt(9))))
      else
        Future.successful(Option.empty)

    })

  override def create(entity: Contract): Future[Option[Contract]] =
    PostgresConnector.withPreparedStatement(insert, pstmt => {
    pstmt.setInt(1, entity.contractNumber)
    pstmt.setString(2, entity.workPosition)
    pstmt.setLong(3, entity.startDate)
    pstmt.setLong(4, entity.endDate)
    pstmt.setInt(5, entity.hoursPerWeek)

      if(EmployeeDao.findByPk(entity.employee.fullName).value.get.get.isEmpty)
        EmployeeDao.create(entity.employee)
    pstmt.setString(6, entity.employee.fullName)

      if(CoffeeHouseDao.findByPk(entity.coffeeHouse.address).value.get.get.isEmpty)
        CoffeeHouseDao.create(entity.coffeeHouse)
    pstmt.setString(7, entity.coffeeHouse.address)

    pstmt.setDouble(8, entity.salary)
    pstmt.setInt(9, entity.vacation)
    pstmt.execute()

    Future.successful(Option(entity))
  })

  override def findByPk(contractNumber: Int): Future[Option[Contract]] =
    PostgresConnector.withPreparedStatement(selectByContractNumber, pstmt => {
      pstmt.setInt(1, contractNumber)

      val result = pstmt.executeQuery()

      if(result.next())
        Future.successful(Option(Contract(result.getInt(1), result.getString(2), result.getLong(3), result.getLong(4), result.getInt(5), null, null, result.getDouble(8), result.getInt(9))))
      else
        Future.successful(Option.empty)

    })

  override def findAll(): Future[Option[List[Contract]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAll)
      convertResultToList(rs,
        result => {
          var employee: Employee = null
          EmployeeDao.findByPk(result.getString(6)).onComplete(empl => employee = empl.get.get)

          var coffeeHouse: CoffeeHouse = null
          CoffeeHouseDao.findByPk(result.getString(7)).onComplete(chouse => coffeeHouse = chouse.get.get)

          Contract(result.getInt(1), result.getString(2), result.getLong(3), result.getLong(4), result.getInt(5), employee, coffeeHouse, result.getDouble(8), result.getInt(9))
        })
    })
  }

  override def delete(id: Int): Future[Option[Int]] = ???

  override def update(entity: Contract): Future[Option[Contract]] = ???
}
