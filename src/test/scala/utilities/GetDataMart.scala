package utilities

import net.liftweb.json.{DefaultFormats, JsonAST, parse}

import scala.io.Source

object GetDataMart {
  val fileName = "src/test/resources/datamarts.json"
  implicit val formats = DefaultFormats
  case class Datamart(martRequestId: Int, columns: Int, records: Int, last: Int, filterCount:Int, field: String, profile: List[String])

  def getElementsFromJson: List[JsonAST.JValue] = {
    (parse(Source.fromFile(fileName).mkString) \\ "datamart").children
  }

  def getMartRequestIdFromJson(columns: Int, records: Int): Int = {
    var martRequestId: Int = 0
    for (json <- getElementsFromJson) {
      val datamart = json.extract[Datamart]
      if (datamart.columns == columns && datamart.records == records)
        martRequestId = datamart.martRequestId
    }
    martRequestId
  }


  def getLastFromJson(columns: Int, records: Int): Int = {
    var last: Int = 0
    for (json <- getElementsFromJson) {
      val datamart = json.extract[Datamart]
      if (datamart.columns == columns && datamart.records == records)
        last = datamart.last
    }
    last
  }

  def getFieldFromJson(columns: Int, records: Int): String = {
    var field: String = null
    for (json <- getElementsFromJson) {
      val datamart = json.extract[Datamart]
      if (datamart.columns == columns && datamart.records == records)
        field = datamart.field
    }
    field
  }

   def getProfileFromJson(columns: Int, records: Int): String = {
    var profile: String = null
    for (json <- getElementsFromJson) {
      val datamart = json.extract[Datamart]
      if (datamart.columns == columns && datamart.records == records) {
        if (datamart.profile.size > 1)
          profile ="[\"" + datamart.profile.mkString("\",\"") + "\"]"
        else
          profile ="\"" + datamart.profile.mkString + "\""
      }
    }
    profile
  }

  def getFilterCountFromJson(martRequestId: Int): Int = {
    var filterCount: Int = 0
    for (json <- getElementsFromJson) {
      val datamart = json.extract[Datamart]
      if (datamart.martRequestId == martRequestId)
        filterCount = datamart.filterCount
    }
    filterCount
  }

  def getProfileFromJson(martRequestId: Int): String = {
    var profile: String = null
    for (json <- getElementsFromJson) {
      val datamart = json.extract[Datamart]
      if (datamart.martRequestId == martRequestId) {
        if (datamart.profile.size > 1)
          profile ="[\"" + datamart.profile.mkString("\",\"") + "\"]"
        else
          profile ="\"" + datamart.profile.mkString + "\""
      }
    }
    profile
  }

}
