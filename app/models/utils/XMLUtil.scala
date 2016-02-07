package models.utils
import scala.xml._
import java.net.URL
import models._

object XMLUtil {
  val levelAboveCategories = "Content"

  def getContentVals(rootNode: Elem): NodeSeq = {
    val nodeSeq: NodeSeq = rootNode \\ levelAboveCategories \ "_"
    return nodeSeq
  }

  def getContentNames(rootNode: Elem) = {
    val content: NodeSeq = getContentVals(rootNode)
    for { x <- content } yield x.label
  }

  def countCats(rootNode: Elem): Int = {
    getContentNames(rootNode).length
  }

  def procProd(node: Node): Seq[OurProduct] = {
    val names: Seq[String] = Seq((node \ "Name").text)
    val UUIDs: Seq[String] = Seq((node \ "UUID").text)
    val descs: Seq[String] = Seq((node \ "Description").text)
    val prices: Seq[String] = Seq((node \ "Price").text)
    val imageThumbURLs: Seq[String] = Seq((node \\ "Thumb").text)
    val imageLargeURLs: Seq[String] = Seq((node \\ "Large").text)
    val itemsList = List(names, descs, imageThumbURLs, imageLargeURLs, UUIDs, prices).transpose
    val productsList = itemsList.map(x => OurProduct(Item(x(0), x(1), new URL(x(2)), new URL(x(3))), x(4), x(5)))
    return productsList
  }

  def procFood(node: Node): Seq[Food] = {
    val names: Seq[String] = Seq((node \ "Name").text)
    val descs: Seq[String] = Seq((node \ "Description").text)
    val imageThumbURLs: Seq[String] = Seq((node \\ "Thumb").text)
    val imageLargeURLs: Seq[String] = Seq((node \\ "Large").text)
    val itemsList = List(names, descs, imageThumbURLs, imageLargeURLs).transpose
    val foodsList = itemsList.map(x => Food(Item(x(0), x(1), new URL(x(2)), new URL(x(3)))))
    return foodsList
  }

  def getDataAsMap(rootNode: Elem): Map[String, Seq[Any]] = {
    val productsXML: NodeSeq = rootNode \ "Products" \ "_"
    val products =
      productsXML.map { product => procProd(product)
      }
    val productsFlat = products.flatten
    //    printf("debug: %s category items have names: %s\n", "Products", productsFlat)

    val foodsXML: NodeSeq = rootNode \ "Food" \ "_"
    val foods =
      foodsXML.map { food => procFood(food)
      }
    val foodsFlat = foods.flatten
    //    printf("debug: %s category items have names: %s\n", "Food", foodsFlat)

    val retMap = Map("Products" -> productsFlat, "Food" -> foodsFlat)
    return retMap
  }

  def main(args: Array[String]): Unit = {
    val loadElem = xml.XML.loadFile("/Users/bentito/workspace/mcl/public/testData.xml")
    println("loaded XML")
    getDataAsMap(loadElem)
  }
}