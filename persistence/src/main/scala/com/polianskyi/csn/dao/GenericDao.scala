package com.polianskyi.csn.dao

import scala.concurrent.Future


trait GenericDao[E, PK] {
  def findByPk(id: PK): Future[Option[E]]
  def findAll(): Future[List[E]]
  def delete(id: PK): Future[Option[E]]
  def create(entity: E): Future[Option[E]]
  def update(entity: E): Future[Option[E]]
}
