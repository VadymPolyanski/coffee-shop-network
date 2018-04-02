package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.Contract

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

  override def findByPk(id: Int): Future[Option[Contract]] = ???

  override def findAll(): Future[Option[List[Contract]]] = ???

  override def delete(id: Int): Future[Option[Int]] = ???

  override def create(entity: Contract): Future[Option[Contract]] = ???

  override def update(entity: Contract): Future[Option[Contract]] = ???
}
