package requests

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object MaterializationRequest {

  def getMaterializationJson(category: String, recordTypes :List[String], fromDate: String, toDate: String, fromTime: String, toTime: String, issueSym: String, firmMpId: String, tags :List[String]): String =
    s"""{"category":"${category}","recordTypes":["${recordTypes.mkString("\",\"")}"],"fromDate":"${fromDate}","toDate":"${toDate}","accessPattern":null,"fromTime":"${fromTime}","toTime":"${toTime}","issueSym":"${issueSym}","firmMpId":"${firmMpId}","allRelatedFirms":false,"tags":["${tags.mkString("\",\"")}"],"preFilters":null}"""

  def postMaterializationProcess(requestName: String, category: String, recordTypes :List[String], fromDate: String, toDate: String, fromTime: String, toTime: String, issueSym: String, firmMpId: String, tags :List[String]): HttpRequestBuilder =
    http(requestName)
    .post(baseUrl + "/materialize/datamart/initiate")
    .body(StringBody(getMaterializationJson(category, recordTypes, fromDate, toDate, fromTime, toTime, issueSym, firmMpId, tags))).asJson
    .check(status is 200)
}