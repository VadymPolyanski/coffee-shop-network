package com.polianskyi.csn.service

import com.polianskyi.csn.CoffeeDrinkHandler.{CoffeeDrinkDeleted, CoffeeDrinkNotFound}
import com.polianskyi.csn.CoffeeHouseHandler.{CoffeeHouseDeleted, CoffeeHouseNotFound}
import com.polianskyi.csn.ContractHandler.{ContractDeleted, ContractNotFound}
import com.polianskyi.csn.EmployeeHandler.{EmployeeDeleted, EmployeeNotFound}
import com.polianskyi.csn.ProductHandler.{ProductDeleted, ProductNotFound}
import com.polianskyi.csn.SalesReportHandler.{SalesReportDeleted, SalesReportNotFound}
import com.polianskyi.csn.domain._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait Protocols extends DefaultJsonProtocol{
  implicit val deleteProductFormat: RootJsonFormat[ProductDeleted] = jsonFormat1(ProductDeleted.apply)
  implicit val productNotFoundFormat: RootJsonFormat[ProductNotFound] = jsonFormat1(ProductNotFound.apply)

  implicit val deleteCoffeeHouseFormat: RootJsonFormat[CoffeeHouseDeleted] = jsonFormat1(CoffeeHouseDeleted.apply)
  implicit val coffeeHouseNotFoundFormat: RootJsonFormat[CoffeeHouseNotFound] = jsonFormat1(CoffeeHouseNotFound.apply)
  implicit val coffeeHouseFormat: RootJsonFormat[CoffeeHouse] = jsonFormat4(CoffeeHouse.apply)


  implicit val deleteEmployeeFormat: RootJsonFormat[EmployeeDeleted] = jsonFormat1(EmployeeDeleted.apply)
  implicit val employeeNotFoundFormat: RootJsonFormat[EmployeeNotFound] = jsonFormat1(EmployeeNotFound.apply)
  implicit val employeeFormat: RootJsonFormat[Employee] = jsonFormat6(Employee.apply)

  implicit val deleteContractFormat: RootJsonFormat[ContractDeleted] = jsonFormat1(ContractDeleted.apply)
  implicit val contractNotFoundFormat: RootJsonFormat[ContractNotFound] = jsonFormat1(ContractNotFound.apply)
  implicit val positionFormat: RootJsonFormat[Position] = jsonFormat4(Position.apply)
  implicit val contractFormat: RootJsonFormat[Contract] = jsonFormat9(Contract.apply)

  implicit val deleteCoffeeDrinkFormat: RootJsonFormat[CoffeeDrinkDeleted] = jsonFormat1(CoffeeDrinkDeleted.apply)
  implicit val coffeeDrinkNotFoundFormat: RootJsonFormat[CoffeeDrinkNotFound] = jsonFormat1(CoffeeDrinkNotFound.apply)
  implicit val productFormat: RootJsonFormat[Product] = jsonFormat4(Product.apply)
  implicit val coffeeDrinkFormat: RootJsonFormat[CoffeeDrink] = jsonFormat5(CoffeeDrink.apply)

  implicit val deleteSalesReportFormat: RootJsonFormat[SalesReportDeleted] = jsonFormat1(SalesReportDeleted.apply)
  implicit val salesReportNotFoundFormat: RootJsonFormat[SalesReportNotFound] = jsonFormat1(SalesReportNotFound.apply)
  implicit val salesReportPKFormat: RootJsonFormat[SalesReportPK] = jsonFormat3(SalesReportPK.apply)
  implicit val salesReportFormat: RootJsonFormat[SalesReport] = jsonFormat5(SalesReport.apply)

  implicit val firstQueryAnswerFormat: RootJsonFormat[FirstQueryAnswer] = jsonFormat3(FirstQueryAnswer.apply)
  implicit val secondQueryAnswerFormat: RootJsonFormat[SecondQueryAnswer] = jsonFormat4(SecondQueryAnswer.apply)
  implicit val thirdQueryAnswerFormat: RootJsonFormat[ThirdQueryAnswer] = jsonFormat4(ThirdQueryAnswer.apply)
  implicit val fourthQueryAnswerFormat: RootJsonFormat[FourthQueryAnswer] = jsonFormat4(FourthQueryAnswer.apply)
  implicit val fivethQueryAnswerFormat: RootJsonFormat[FivethQueryAnswer] = jsonFormat6(FivethQueryAnswer.apply)
  implicit val sixthQueryAnswerFormat: RootJsonFormat[SixthQueryAnswer] = jsonFormat3(SixthQueryAnswer.apply)
  implicit val seventhQueryAnswerFormat: RootJsonFormat[SeventhQueryAnswer] = jsonFormat2(SeventhQueryAnswer.apply)
  implicit val eightthQueryAnswerFormat: RootJsonFormat[EighthQueryAnswer] = jsonFormat4(EighthQueryAnswer.apply)
  implicit val nainthQueryAnswerFormat: RootJsonFormat[NainthQueryAnswer] = jsonFormat4(NainthQueryAnswer.apply)
  implicit val tenthQueryAnswerFormat: RootJsonFormat[TenthQueryAnswer] = jsonFormat3(TenthQueryAnswer.apply)
}
