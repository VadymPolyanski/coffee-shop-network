package com.polianskyi.csn.domain

import java.sql.Date

final case class CoffeeHouse(address: String, space: Double, rentalPrice: Double, mobileNumber: String)
final case class Employee(fullName: String,
                          birthdayDate: Date,
                          mobileNumber: String,
                          address: String,
                          passport: String,
                          sex: String)
