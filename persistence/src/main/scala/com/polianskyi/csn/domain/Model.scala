package com.polianskyi.csn.domain

final case class CoffeeHouse(address: String, space: Double, rentalPrice: Double, mobileNumber: String)
final case class CoffeeDrink(name: String, price: Double, nativePrice: Double, products: List[Product], description: String)
final case class SalesReport(coffeeDrink: CoffeeDrink, employee: Employee, priceWithVat: Double, saleDate: Long, coffeeHouse: CoffeeHouse)
final case class Product(name: String, price: Double, unitOfMeasurement: String, description: String)
final case class Position(name: String, avgSalary: Double, priority: Int, description: String)

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

final case class FirstQueryAnswer(fullName: String, company:String, product:String)
final case class SecondQueryAnswer(fullName: String, birthdayDate: Long, mobileNumber: String, sex: String)
final case class ThirdQueryAnswer(coffeeDrink: String, employee: String, price: Double, description: String)
final case class FourthQueryAnswer(address: String, space: Double, mobileNumber: String, sales: Int)
final case class FivethQueryAnswer(contractNumber: Long, employee: String, position: String, startDate: Long, hoursPerWeek: Int, salary: Double)
final case class SixthQueryAnswer(coffeeDrink: String, saleDate: Long, priceWithVat: Double)
final case class SeventhQueryAnswer(name: String, avgSalary: Double)
final case class EightthQueryAnswer(fullName: String, birthdayDate: Long, mobileNumber: String, sex: String)
final case class NainthQueryAnswer(address: String, mobileNumber: String, space: Double, sales: Double)
final case class TenthQueryAnswer(fullName: String, birthdayDate: Long, sales: Int)