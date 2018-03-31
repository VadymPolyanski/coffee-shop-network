package com.polianskyi.csn.domain

import java.sql.Date

final case class CoffeeHouse(address: String, space: Double, rentalPrice: Double, mobileNumber: String)
final case class CoffeeDrink(name: String, price: Double, nativePrice: Double, products: List[Product], description: String)
final case class Product(name: String, price: Double, unit_of_measurement: String, description: String)
final case class Employee(fullName: String,
                          birthdayDate: Date,
                          mobileNumber: String,
                          address: String,
                          passport: String,
                          sex: String)
