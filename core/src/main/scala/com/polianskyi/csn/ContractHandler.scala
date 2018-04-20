package com.polianskyi.csn

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import com.polianskyi.csn.dao.ContractDao._
import com.polianskyi.csn.domain.{CoffeeHouse, Contract, Employee}

import scala.concurrent.ExecutionContextExecutor

object ContractHandler {

  def props(): Props = Props(new ContractHandler())

  case class Create(contractNumber: Int,
                    workPosition: String,
                    startDate: Long,
                    endDate: Long,
                    hoursPerWeek: Int,
                    employee: Employee,
                    coffeeHouse: CoffeeHouse,
                    salary: Double,
                    vacation: Int)
  case class Update(contractNumber: Int,
                    workPosition: String,
                    startDate: Long,
                    endDate: Long,
                    hoursPerWeek: Int,
                    employee: Employee,
                    coffeeHouse: CoffeeHouse,
                    salary: Double,
                    vacation: Int)
  case class GetContract(contractNumber: Int)
  case class GetContractByEmployee(employee: String)
  case class GetAllContracts()
  case class DeleteContract(contractNumber: Int)
  case class ContractNotFound(contractNumber: String)
  case class ContractDeleted(contractNumber: Int)
  case class ContractCreatingError(contractNumber: Int, message: String)
}



class ContractHandler extends Actor {
  import ContractHandler._
  implicit val ec: ExecutionContextExecutor = context.dispatcher
  override def receive: Receive = {
    case Create(contractNumber, workPosition, startDate, endDate, hoursPerWeek, employee, coffeeHouse, salary, vacation) =>
      val _sender = sender()
      create(Contract(contractNumber, workPosition, startDate, endDate, hoursPerWeek, employee, coffeeHouse, salary, vacation)).foreach {
        case Some(i) => _sender ! Contract(i.contractNumber, i.workPosition, i.startDate, i.endDate, i.hoursPerWeek, i.employee, i.coffeeHouse, i.salary, i.vacation)
        case None => _sender ! ContractCreatingError(contractNumber, "Can't create contract")
      }

    case Update(contractNumber, workPosition, startDate, endDate, hoursPerWeek, employee, coffeeHouse, salary, vacation) =>
      update(Contract(contractNumber, workPosition, startDate, endDate, hoursPerWeek, employee, coffeeHouse, salary, vacation)) pipeTo sender()

    case GetAllContracts() =>
      val _sender = sender()
      findAll().foreach {
        case Some(i) => _sender ! i
        case _ => Nil
      }

    case GetContract(contractNumber) =>
      val _sender = sender()
      findByPk(contractNumber).foreach {
        case Some(i) => _sender ! Contract(i.contractNumber, i.workPosition, i.startDate, i.endDate, i.hoursPerWeek, i.employee, i.coffeeHouse, i.salary, i.vacation)
        case None => _sender ! ContractNotFound(contractNumber.toString)
      }

    case GetContractByEmployee(fullName) =>
      val _sender = sender()
      findByEmployee(fullName).foreach {
        case Some(i) => _sender ! Contract(i.contractNumber, i.workPosition, i.startDate, i.endDate, i.hoursPerWeek, i.employee, i.coffeeHouse, i.salary, i.vacation)
        case None => _sender ! ContractNotFound("0")
      }

    case DeleteContract(fullName) =>
      val _sender = sender()
      delete(fullName).foreach {
        case i if i != null => _sender ! ContractDeleted(fullName)
        case _ => _sender ! ContractNotFound(fullName.toString)
      }
  }
}
