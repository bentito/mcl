package models

import play._
import scala.xml._

class LoadData {
    val path = Play.application().path().toString()
    val sep = java.io.File.separatorChar
    val fileName : String = api.Play.current.configuration.getString("xml.filename").getOrElse("YouNeedTheXMLFileNamedInConf")
    val loadElem = xml.XML.loadFile(path + sep + "public" + sep + fileName)
}