/*
 * Meredith Content Licensing Code Challenge
 * 
 * Brett Tofel 2016
 */
package models

import models.utils._
import java.util.{ Date }
import java.net.URL

import play.api.Play.current
import play.api.cache._

import scala.xml._
import scala.language.postfixOps

case class Company(id: Option[Long] = None, name: String) // cruft from CRUD example TODO: Remove

//TODO: Need polymorphic approach, stymied by mapping to apply,unapply paradigm for now,
//TODO: both these classes extend abstract class Items
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
  imageLargeURL: String)

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object OurProduct {

  /**
   * Retrieve a product from the id.
   */
  def findById(id: Long): Option[OurProduct] = {
    val dummyProduct = OurProduct("dummyName", "dummyDesc", "http://foo.com", "http://foo.com", "UUIDfoo", "priceFoo")
    val products = dummyProduct
    Option(products)
  }

  /**
   * Return a page of (OurProduct,Company).
   *
   * @param page Page to display
   * @param pageSize Number of products per page
   * @param orderBy A property used for sorting
   * @param filter Filter applied on the name column
   */
  def list(page: Int = 0, pageSize: Int = 4, orderBy: Int = 1, filter: String = "%"): Page[(OurProduct, Option[Company])] = {

    val offest = pageSize * page

    // Load XML from cache, do this only once TODO: Test this more
    val loadElem: Elem = Cache.getOrElse("load.elem") {
      val loadData = new models.LoadData
      val cachedLoadElem = loadData.loadElem
      Cache.set("load.elem", cachedLoadElem)
      println("debug: loaded XML from file")
      cachedLoadElem
    }

    val records = buildRecords(loadElem)

    Page(records, page, offest, 1)
  }

  def buildRecords(loadElem: Elem) = {
    val catList = XMLUtil.getContentNames(loadElem)
    val dataMap = XMLUtil.getDataAsMap(loadElem)
    val productList = dataMap("Products").map(x => x.asInstanceOf[OurProduct])
    val foodList = dataMap("Food").map(x => x.asInstanceOf[Food])

    // Note: product.item.name works to ref this param TODO: bring back Item or some polymorphic help
    for (product <- productList) yield (product, Option(Company(Option(1003L), "CompanyFoo")))
  }

  /**
   * TODO: Can remove.
   *
   * @param id The computer id
   * @param computer The computer values.
   */
  def update(id: Long, product: OurProduct) = {
    true
  }

  /**
   * TODO: Can remove.
   *
   * @param computer The computer values.
   */
  def insert(product: OurProduct) = {

    true
  }

  /**
   * TODO: Can remove.
   *
   * @param id Id of the computer to delete.
   */
  def delete(id: Long) = {
    true
  }

}

//TODO: Can remove.
object Company {

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

