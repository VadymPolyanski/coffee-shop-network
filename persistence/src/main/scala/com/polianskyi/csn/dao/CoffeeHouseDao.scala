package com.polianskyi.csn.dao

import com.polianskyi.csn.ConverterUtil._
import com.polianskyi.csn.domain.CoffeeHouse
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object CoffeeHouseDao extends GenericDao[CoffeeHouse, String] {

  private val insert: String = "INSERT INTO coffee_houses (address, space, rental_price, mobile_number)" +
    "\nVALUES (?, ?, ?, ?);"

  private val update: String = "UPDATE coffee_houses SET address=?, space=?, rental_price=?, mobile_number=?" +
    "\nWHERE address=?;"

  private val delete: String = "DELETE FROM coffee_houses WHERE address=?;"

  private val selectByAddress: String = "SELECT address, space, rental_price, mobile_number " +
    "FROM coffee_houses WHERE address=?;"

  private val selectAll: String = "SELECT * " +
    "FROM coffee_houses;"

  private val selectAllAddresses: String = "SELECT address " +
    "FROM coffee_houses;"


  override def findByPk(address: String): Future[Option[CoffeeHouse]] = {
    PostgresConnector.withPreparedStatement(selectByAddress, pstmt => {
      pstmt.setString(1, address)

      val result = pstmt.executeQuery()

      if(result.next())
        Future.successful(Option(CoffeeHouse(result.getString(1), result.getDouble(2), result.getDouble(3), result.getString(4))))
      else
        Future.successful(Option.empty)

    })
  }

  override def findAll(): Future[Option[List[CoffeeHouse]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAll)
      convertResultToList(rs,
        result => CoffeeHouse(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4)))
    })
  }

  def findAllAddresses(): Future[Option[List[String]]] = {
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAllAddresses)
      convertResultToList(rs, result => result.getString(1))
    })
  }

  override def delete(address: String): Future[Option[String]] = {
    PostgresConnector.withPreparedStatement(delete, pstmt => {
      pstmt.setString(1, address)
      if (pstmt.execute())
        Future.successful(Option(address))
       else
        Future.successful(Option.empty)
    })
  }

  override def create(entity: CoffeeHouse): Future[Option[CoffeeHouse]] =
    PostgresConnector.withPreparedStatement(insert, pstmt => {
      pstmt.setString(1, entity.address)
      pstmt.setDouble(2, entity.space)
      pstmt.setDouble(3, entity.rentalPrice)
      pstmt.setString(4, entity.mobileNumber)
      pstmt.execute()

      Future.successful(Option(entity))
    })

  override def update(entity: CoffeeHouse): Future[Option[CoffeeHouse]] = {
    PostgresConnector.withPreparedStatement(update, pstmt => {
      pstmt.setString(1, entity.address)
      pstmt.setDouble(2, entity.space)
      pstmt.setDouble(3, entity.rentalPrice)
      pstmt.setString(4, entity.mobileNumber)
      pstmt.setString(5, entity.address)
      pstmt.execute()

      Future.successful(Option(entity))
    })
  }
}
