package com.polianskyi.csn.dao

trait GenericDao[E, PK] {
  def findById(id: PK): E
  def findAll(): List[E]
  def delete(id: PK): E
  def create(entity: E): E
  def update(entity: E): E
}
