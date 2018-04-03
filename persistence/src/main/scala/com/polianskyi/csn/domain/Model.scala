package com.polianskyi.csn.domain

final case class CoffeeHouse(address: String, space: Double, rentalPrice: Double, mobileNumber: String)
final case class CoffeeDrink(name: String, price: Double, nativePrice: Double, products: List[Product], description: String)
final case class SalesReport(coffeeDrink: CoffeeDrink, employee: Employee, priceWithVat: Double, saleDate: Long, coffeeHouse: CoffeeHouse)
final case class Product(name: String, price: Double, unitOfMeasurement: String, description: String)
final case class Position(name: String, avgSalary: Double, priority: Integer, description: String)

final case class Contract(contractNumber: Int,
                          workPosition: String,
                          startDate: Long,
                          endDate: Long,
                          hoursPerWeek: Int,
                          employee: Employee,
                          coffeeHouse: CoffeeHouse,
                          salary: Double,
                          vacation: Int)

final case class Employee(fullName: String,
                          birthdayDate: Long,
                          mobileNumber: String,
                          address: String,
                          passport: String,
                          sex: String)

final case class SalesReportPK(coffeeDrink: String, employee: String, saleDate: Long)