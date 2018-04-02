package com.polianskyi.csn.dao

import com.polianskyi.csn.domain.{SalesReport, SalesReportPK}

import scala.concurrent.Future

object SalesReportDao extends GenericDao[SalesReport, SalesReportPK] {
  override def findByPk(id: SalesReportPK): Future[Option[SalesReport]] = ???

  override def findAll(): Future[Option[List[SalesReport]]] = ???

  override def delete(id: SalesReportPK): Future[Option[SalesReportPK]] = ???

  override def create(entity: SalesReport): Future[Option[SalesReport]] = ???

  override def update(entity: SalesReport): Future[Option[SalesReport]] = ???
}
