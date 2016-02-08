/*
 * Meredith Content Licensing Code Challenge
 * 
 * Brett Tofel 2016
 */
 package models
 

import play._
import scala.xml._

/* handles getting XML file location from configuration file */
class LoadData {
    val path = Play.application().path().toString()
    val sep = java.io.File.separatorChar
    val fileName : String = api.Play.current.configuration.getString("xml.filename").getOrElse("YouNeedTheXMLFileNamedInConf")
    val loadElem = xml.XML.loadFile(path + sep + "public" + sep + fileName)
}