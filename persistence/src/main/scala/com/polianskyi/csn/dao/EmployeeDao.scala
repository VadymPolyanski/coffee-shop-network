package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.Employee

import scala.concurrent.Future

object EmployeeDao extends GenericDao[Employee, String] {
  override def findByPk(id: String): Future[Option[Employee]] = ???

  override def findAll(): Future[List[Employee]] = ???

  override def delete(id: String): Future[Option[Employee]] = ???

  override def create(entity: Employee): Future[Option[Employee]] = ???

  override def update(entity: Employee): Future[Option[Employee]] = ???
}
