package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.Employee

object EmployeeDao extends GenericDao[Employee, String] {
  override def findById(id: String): Employee = ???

  override def findAll(): List[Employee] = ???

  override def delete(id: String): Employee = ???

  override def create(entity: Employee): Employee = ???

  override def update(entity: Employee): Employee = ???
}
