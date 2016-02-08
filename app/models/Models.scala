package models

import models.utils._
import java.util.{ Date }
import java.net.URL

import play.api.Play.current
import play.api.cache._

import scala.xml._
import scala.language.postfixOps

case class Company(id: Option[Long] = None, name: String)
//case class Computer(id: Option[Long] = None, name: String, introduced: Option[Date], discontinued: Option[Date], companyId: Option[Long])

//case class Item(
//  name: String,
//  description: String,
//  imageThumbURL: URL,
//  imageLargeURL: URL)

case class OurProduct(
  name: String,
  description: String,
  imageThumbURL: String,
  imageLargeURL: String,
  UUID: String,
  price: String)

case class Food(
  name: String,
  description: String,
  imageThumbURL: String,
  imageLargeURL: String
)

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object OurProduct {

  // -- Parsers

  /**
   * Parse a Computer from a ResultSet
   */
  //  val simple = {
  //    get[Option[Long]]("computer.id") ~
  //    get[String]("computer.name") ~
  //    get[Option[Date]]("computer.introduced") ~
  //    get[Option[Date]]("computer.discontinued") ~
  //    get[Option[Long]]("computer.company_id") map {
  //      case id~name~introduced~discontinued~companyId => Computer(id, name, introduced, discontinued, companyId)
  //    }
  //  }

  /**
   * Parse a (Computer,Company) from a ResultSet
   */
  //  val withCompany = Computer.simple ~ (Company.simple ?) map {
  //    case computer~company => (computer,company)
  //  }

  // -- Queries

  /**
   * Retrieve a computer from the id.
   */
  def findById(id: Long): Option[OurProduct] = {
    //    DB.withConnection { implicit connection =>
    //      SQL("select * from computer where id = {id}").on('id -> id).as(Computer.simple.singleOpt)
    //    }
//    val dummyItem = Item("dummyName", "dummyDesc", new URL("http://foo.com"), new URL("http://foo.com"))
    val dummyProduct = OurProduct("dummyName", "dummyDesc", "http://foo.com", "http://foo.com", "UUIDfoo", "priceFoo")
    val products = dummyProduct
    Option(products)
  }

  /**
   * Return a page of (Computer,Company).
   *
   * @param page Page to display
   * @param pageSize Number of computers per page
   * @param orderBy Computer property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(OurProduct, Option[Company])] = {

    val offest = pageSize * page

    val loadElem: Elem = Cache.getOrElse("load.elem") {
      val loadData = new models.LoadData
      val cachedLoadElem = loadData.loadElem
      Cache.set("load.elem", cachedLoadElem)
      println("debug: loaded XML from file")
      cachedLoadElem
    }

    val records = buildRecords(loadElem)

    //    Computer(id: Option[Long] = None, name: String, introduced: Option[Date], discontinued: Option[Date], companyId: Option[Long])
//    val dummyItem = Item("dummyName", "dummyDesc", new URL("http://foo.com"), new URL("http://foo.com"))
//    val dummyProduct = OurProduct("dummyName", "dummyDesc", "http://foo.com", "http://foo.com", "UUIDfoo", "priceFoo")
//    val products = dummyProduct
//    val company = Company(Option(1003L), "CompanyFoo")
    //    val seqCC = Seq((computers, Option(company)))
    //    println(seqCC)

    Page(records, page, offest, 1)
  }

  def buildRecords(loadElem: Elem) = {
    val catList = XMLUtil.getContentNames(loadElem)
    val dataMap = XMLUtil.getDataAsMap(loadElem)
    val productList = dataMap("Products").map(x => x.asInstanceOf[OurProduct])
    val foodList = dataMap("Food").map(x => x.asInstanceOf[Food])

    // NB: product.item.name works to ref this param 
    for (product <- productList) yield (product, Option(Company(Option(1003L), "CompanyFoo")))
  }

  /**
   * Update a computer.
   *
   * @param id The computer id
   * @param computer The computer values.
   */
  def update(id: Long, product: OurProduct) = {
    //    DB.withConnection { implicit connection =>
    //      SQL(
    //        """
    //          update computer
    //          set name = {name}, introduced = {introduced}, discontinued = {discontinued}, company_id = {company_id}
    //          where id = {id}
    //        """
    //      ).on(
    //        'id -> id,
    //        'name -> computer.name,
    //        'introduced -> computer.introduced,
    //        'discontinued -> computer.discontinued,
    //        'company_id -> computer.companyId
    //      ).executeUpdate()
    //    }
    true
  }

  /**
   * Insert a new computer.
   *
   * @param computer The computer values.
   */
  def insert(product: OurProduct) = {
    //    DB.withConnection { implicit connection =>
    //      SQL(
    //        """
    //          insert into computer values (
    //            (select next value for computer_seq), 
    //            {name}, {introduced}, {discontinued}, {company_id}
    //          )
    //        """
    //      ).on(
    //        'name -> computer.name,
    //        'introduced -> computer.introduced,
    //        'discontinued -> computer.discontinued,
    //        'company_id -> computer.companyId
    //      ).executeUpdate()
    //    }
    true
  }

  /**
   * Delete a computer.
   *
   * @param id Id of the computer to delete.
   */
  def delete(id: Long) = {
    //    DB.withConnection { implicit connection =>
    //      SQL("delete from computer where id = {id}").on('id -> id).executeUpdate()
    //    }
    true
  }

}

object Company {

  /**
   * Parse a Company from a ResultSet
   */
  //  val simple = {
  //    get[Option[Long]]("company.id") ~
  //    get[String]("company.name") map {
  //      case id~name => Company(id, name)
  //    }
  //  }

  /**
   * Construct the Map[String,String] needed to fill a select options set.
   */
  def options: Seq[(String, String)] = {
    //    DB.withConnection { implicit connection =>
    //    SQL("select * from company order by name").as(Company.simple *).
    //      foldLeft[Seq[(String, String)]](Nil) { (cs, c) => 
    //        c.id.fold(cs) { id => cs :+ (id.toString -> c.name) }
    //      }
    Seq(("CompanyOpt1", "CompanyOpt2"))
  }

}

