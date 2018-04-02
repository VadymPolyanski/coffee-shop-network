package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.EmployeeDao._
import com.polianskyi.csn.domain.Employee

import scala.concurrent.ExecutionContextExecutor

object EmployeeHandler {

  def props(): Props = Props(new EmployeeHandler())

  case class Create(fullName: String,
                    birthdayDate: Long,
                    mobileNumber: String,
                    address: String,
                    passport: String,
                    sex: String)
  case class Update(fullName: String,
                    birthdayDate: Long,
                    mobileNumber: String,
                    address: String,
                    passport: String,
                    sex: String)
  case class GetEmployee(address: String)
  case class GetAllEmployees()
  case class GetAllEmployeesByCoffeeHouse(address: String)
  case class DeleteEmployee(fullName: String)
  case class EmployeeNotFound(fullName: String)
  case class EmployeeDeleted(fullName: String)
  case class EmployeeCreatingError(fullName: String, message: String)
}


class EmployeeHandler extends Actor {
  import EmployeeHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(fullName, birthdayDate, mobileNumber, address, passport, sex) =>
      val _sender = sender()
      create(Employee(fullName, birthdayDate, mobileNumber, address, passport, sex)).foreach {
        case Some(i) => _sender ! Employee(i.fullName, i.birthdayDate, i.mobileNumber, i.address, i.passport, i.sex)
        case None => _sender ! EmployeeCreatingError(fullName, "Can't create coffee drink")
      }

    case Update(fullName, birthdayDate, mobileNumber, address, passport, sex) => update(Employee(fullName, birthdayDate, mobileNumber, address, passport, sex)) pipeTo sender()

    case GetAllEmployees() =>
      val _sender = sender()
      findAll().foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case GetEmployee(fullName) =>
      val _sender = sender()
      findByPk(fullName).foreach {
        case Some(i) => _sender ! Employee(i.fullName, i.birthdayDate, i.mobileNumber, i.address, i.passport, i.sex)
        case None => _sender ! EmployeeNotFound(fullName)
      }

    case GetAllEmployeesByCoffeeHouse(address) =>
      val _sender = sender()
      findAllByCoffeeHouse(address).foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case DeleteEmployee(fullName) =>
      val _sender = sender()
      delete(fullName).foreach {
        case i if i != null => _sender ! EmployeeDeleted(fullName)
        case _ => _sender ! EmployeeNotFound(fullName)
      }
  }
}
