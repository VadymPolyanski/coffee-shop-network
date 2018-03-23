package com.polianskyi.csn.dao

trait GenericDao[E, ID] {
  def findById(id: ID): E
  def findAll(): List[E]
  def delete(id: ID): E
  def create(entity: E): E
  def update(entity: E): E
}
