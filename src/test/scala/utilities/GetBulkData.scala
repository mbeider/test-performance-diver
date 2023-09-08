package utilities

import net.liftweb.json.{DefaultFormats, JsonAST, parse}
import scala.io.Source

object GetBulkData {
  val fileName = "src/test/resources/requests.json"
  implicit val formats = DefaultFormats
  case class RequestData( recordTypes: List[String], fromDate: String, toDate: String, fromTime: String, toTime: String, issueSym: String, issueId: String, firmMpId: String, allRelatedFirms: Boolean, useOatsProcessingDate: Boolean, preFilters: String, tags: String) {
    override def equals(that: Any): Boolean = ???
  }

  def getElementsFromJson: List[JsonAST.JValue] = {
    (parse(Source.fromFile(fileName).mkString) \\ "request").children
  }

}
