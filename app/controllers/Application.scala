/*
 * Meredith Content Licensing Code Challenge
 * 
 * Requirements:
 * 1. The application will compile
 * 2. The root route `/` of the application will be a browse view that displays:
 *       1. The ability to choose how many items of content to show per page
 *       2. The ability to filter content by its type
 *       3. The ability to move to a "next" or "previous" page as required to see more content
 *       4. Content shown on this page should not show the description, think of it as a browse view like you'd find 
 *           on an online shopping site.
 * 3. A detail view `/detail/{contentType}/{id}` that displays:
 *       1. Links to the previous page
 *       2. Information about the content being displayed
 * 4. The project should use the bootstrap CSS v3.3.5 [2] framework for style and valid HTML
 * 5. The project should load the provided XML 
 *       1. The start up class [3] should _not_ be in the default package and should reside with other controllers or
 *           configuration classes. 
 *       2. The data should be loaded _only_ once on start up.
 *       3. The file to be loaded should be specified by the application configuration
 * 6. Error Pages such as 404 should use a non-default page and hide internal details. 
 * 
 * Brett Tofel 1/31/2016 
 */
package controllers

import play.api.mvc._
import play._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._

import views._
import models._

object Application extends Controller {
    /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.Application.list(1, 2, "foo"))
  
   /**
   * Describe the computer form (used in both edit and create screens).
   */ 
  val computerForm = Form(
    mapping(
      "id" -> ignored(None:Option[Long]),
      "name" -> nonEmptyText,
      "introduced" -> optional(date("yyyy-MM-dd")),
      "discontinued" -> optional(date("yyyy-MM-dd")),
      "company" -> optional(longNumber)
    )(Computer.apply)(Computer.unapply)
  )
  
//  def welcome = Action {
//    val loadData = new models.LoadData
//    val loadElem = loadData.loadElem
//    Cache.set("load.elem", loadElem) // val cachedLoadElem = Cache.get("load.elem")  
//    // can get the cached XML from anywhere with above get() call. 
//    val catCount = models.utils.XMLUtil.countCats(loadElem)
//    val catList = models.utils.XMLUtil.getContentNames(loadElem)
//    //    println("cats: " + models.utils.XMLUtil.getContentName(loadElem))
//    Ok(views.html.home("MCL Code Challenge", catList))
//  }

  case class catCountRequest(catCount: String)

  def getCatCount = Action { implicit request =>
    val catCountRequest = initForm.bindFromRequest.get
    Ok(s"Category Count Requested: '${catCountRequest.catCount}'")
  }

  def initForm = Form(mapping("catCount" -> text)(catCountRequest.apply)(catCountRequest.unapply))

    /**
   * Handle default path requests, redirect to computers list
   */  
  def index = Action { Home }
  
    /**
   * Display the paginated list of computers.
   *
   * @param page Current page number (starts from 0)
   * @param orderBy Column to be sorted
   * @param filter Filter applied on computer names
   */
  def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
    Ok(html.list(
      Computer.list(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter
    ))
  }
  
    /**
   * Display the 'new computer form'.
   */
  def create = Action {
    Ok(views.html.createForm(computerForm, Company.options))
  }
  
    /**
   * Handle the 'new computer form' submission.
   */
  def save = Action { implicit request =>
    computerForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createForm(formWithErrors, Seq(("fooCoOpts1","fooCoOpt2")))),
      computer => {
        Computer.insert(computer)
        Home.flashing("success" -> "Computer %s has been created".format(computer.name))
      }
    )
  }
  
   /**
   * Display the 'edit form' of a existing Computer.
   *
   * @param id Id of the computer to edit
   */
  def edit(id: Long) = Action {
    Computer.findById(id).map { computer =>
      Ok(html.editForm(id, computerForm.fill(computer), Company.options))
    }.getOrElse(NotFound)
  }
  
   /**
   * Handle the 'edit form' submission 
   *
   * @param id Id of the computer to edit
   */
  def update(id: Long) = Action { implicit request =>
    computerForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.editForm(id, formWithErrors, Company.options)),
      computer => {
        Computer.update(id, computer)
        Home.flashing("success" -> "Computer %s has been updated".format(computer.name))
      })
  }
  
    /**
   * Handle computer deletion.
   */
  def delete(id: Long) = Action {
    Computer.delete(id)
    Home.flashing("success" -> "Computer has been deleted")
  }
}