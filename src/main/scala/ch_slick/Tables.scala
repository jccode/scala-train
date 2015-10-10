package ch_slick;

import slick.driver.MySQLDriver.api._

class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
  def id = column[Int]("SUP_ID", O.PrimaryKey)
  def name = column[String]("SUP_NAME")
  def street = column[String]("STREET")
  def city = column[String]("CITY")
  def state = column[String]("STATE")
  def zip = column[String]("ZIP")

  def * = (id, name, street, city, state, zip)
}

class Coffee(tag: Tag) extends Table[(String, Int, Double, Int, Int)](tag, "COFFEE") {
  def name = column[String]("COF_NAME", O.PrimaryKey, O.SqlType("varchar(100)"))
  def supID = column[Int]("SUP_ID")
  def price = column[Double]("PRICE")
  def sales = column[Int]("SALES")
  def total = column[Int]("TOTAL")

  def * = (name, supID, price, sales, total)

  // A reified foreign key relation that can be navigated to create a join
  def supplier = foreignKey("SUP_FK", supID, TableQuery[Suppliers])(_.id)
}

