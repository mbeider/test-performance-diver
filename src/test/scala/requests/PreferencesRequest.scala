package requests

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object PreferencesRequest {

  def pickListFilterJson(compare: String, fieldPicklist: String, valuesPicklist: List[String], operatorPicklist: String): String =
    s"""{"filters":{"compare":"${compare}","children":[{"compare":"or","children":[{"compare":"${compare}","children":[{"field":"${fieldPicklist}","type":"picklist","value":["${valuesPicklist.mkString(",")}"],"operator":"${operatorPicklist}"}]}]}]}}"""

  def pickListAndStringFilterJson(compare: String, fieldPicklist: String, valuesPicklist: List[String], operatorPicklist: String, fieldString: String, valueString: String, operatorString: String): String =
    s"""{"filters":{"compare":"${compare}","children":[{"compare":"or","children":[{"compare":"${compare}","children":[{"field":"${fieldPicklist}","type":"picklist","value":["${valuesPicklist.mkString(",")}"],"operator":"${operatorPicklist}"},{"compare":"${compare}","children":[{"field":"${fieldString}","type":"string","value":"${valueString}","operator":"${operatorString}"}]}]}]}]}}"""

  def sortJson(field: String, dir: String): String =
    s"""{"sorts":"[{\\"field\\":\\"${field}\\",\\"dir\\":\\"${dir}\\"}]"}"""

  def emptySortJson(): String = """{"sorts":"[]"}"""

  def getPreference(requestName: String, martId:String, userId:String, userViewId: Long): ChainBuilder = {
    exec(http(requestName)
      .get(baseUrl + s"/services/pref/${martId}.${userId}.${userViewId}")
      .queryParam("cb", System.currentTimeMillis.toString)
      .check(status is 200))
  }

  def putPreferenceFilters(requestName: String, martId:String, userId:String, userViewId: Long, customFilter: String): ChainBuilder = {
    exec(http(requestName)
      .put(baseUrl + s"/services/pref/${martId}.${userId}.${userViewId}/filters")
      .body(StringBody(customFilter)).asJson
      .check(status is 200))
  }

  def putPreferenceSorts(requestName: String, martId:String, userId:String, userViewId: Long, customSort: String): ChainBuilder = {
    exec(http(requestName)
      .put(baseUrl + s"/services/pref/${martId}.${userId}.${userViewId}/sorts")
      .body(StringBody(customSort)).asJson
      .check(status is 200))
  }
}
