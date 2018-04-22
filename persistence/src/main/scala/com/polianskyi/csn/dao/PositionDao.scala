package com.polianskyi.csn.dao

import com.polianskyi.csn.ConverterUtil.convertResultToList
import com.polianskyi.csn.domain.Position
import com.polianskyi.csn.system.PostgresConnector

import scala.concurrent.Future

object PositionDao extends GenericDao[Position, String] {

  private val selectAll: String = "SELECT * " +
    "FROM positions;"

  private val delete: String = "DELETE FROM positions " +
    "WHERE name = ?;"

  private val insert: String = "INSERT INTO public.positions(name, avg_salary, priority, description)\n" +
    "VALUES (?, ?, ?, ?);"


  override def findByPk(id: String): Future[Option[Position]] = ???

  override def findAll(): Future[Option[List[Position]]] =
    PostgresConnector.withStatement(stmt => {
      val rs = stmt.executeQuery(selectAll)
      convertResultToList(rs,
        result => Position(result.getString(1), result.getDouble(2), result.getInt(3), result.getString(4)))
    })

  override def create(entity: Position): Future[Option[Position]] =
    PostgresConnector.withPreparedStatement(insert, pstmt => {
    pstmt.setString(1, entity.name)
    pstmt.setDouble(2, entity.avgSalary)
    pstmt.setInt(3, entity.priority)
    pstmt.setString(4, entity.description)
    pstmt.execute()

    Future.successful(Option(entity))
  })

  override def delete(name: String): Future[Option[String]] =
    PostgresConnector.withPreparedStatement(delete, pstmt => {
    pstmt.setString(1, name)
    if (pstmt.execute())
      Future.successful(Option(name))
    else
      Future.successful(Option.empty)
  })

  override def update(entity: Position): Future[Option[Position]] = ???
}
