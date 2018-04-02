package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.Position

import scala.concurrent.Future

object PositionDao extends GenericDao[Position, String] {
  override def findByPk(id: String): Future[Option[Position]] = ???

  override def findAll(): Future[Option[List[Position]]] = ???

  override def delete(id: String): Future[Option[String]] = ???

  override def create(entity: Position): Future[Option[Position]] = ???

  override def update(entity: Position): Future[Option[Position]] = ???
}
