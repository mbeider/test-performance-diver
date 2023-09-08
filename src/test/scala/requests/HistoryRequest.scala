package requests

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object HistoryRequest {

   def getHistoryQueryNoFilterJson(page: Int, pageSize :Int, types: List[String]): String =
    s"""{"page":${page},"pageSize":${pageSize},"types":["${types.mkString("\",\"")}"],"filters":{"compare":"AND", "children":[]}}"""

  def getHistoryQueryFilterStringJson(page: Int, pageSize :Int, types: List[String], compare: String, field: String, operator: String, user: String): String =
    s"""{"page":${page},"pageSize":${pageSize},"types":["${types.mkString("\",\"")}"],"filters":{"compare":"${compare}", "children":[{"field":"${field}","operator":"${operator}","type":"string","value":"${user}"}]}}"""

  def getHistoryQueryFilterStringAndPickListJson(page: Int, pageSize :Int, types: List[String], compare: String, fieldString: String, operatorString: String, user: String, fieldPicklist: String, operatorPicklist: String, statusList: List[String]): String =
    s"""{"page":${page},"pageSize":${pageSize},"types":["${types.mkString("\",\"")}"],"filters":{"compare":"${compare}", "children":[{"field":"${fieldString}","operator":"${operatorString}","type":"string","value":"${user}"},{"field":"${fieldPicklist}","operator":"${operatorPicklist}","type":"picklist","value":["${statusList.mkString("\",\"")}"]}]}}"""

  def getHistoryQueryFilterStringAndStringJson(page: Int, pageSize :Int, types: List[String], compare: String, fieldStringUser: String, operatorStringUser: String, user: String, fieldStringTitle: String, operatorStringTitle: String, query: String): String =
    s"""{"page":${page},"pageSize":${pageSize},"types":["${types.mkString("\",\"")}"],"filters":{"compare":"${compare}", "children":[{"field":"${fieldStringUser}","operator":"${operatorStringUser}","type":"string","value":"${user}"},{"field":"${fieldStringTitle}","operator":"${operatorStringTitle}","type":"string","value":"${query}"}]}}"""

 def getHistoryQueryThreeFiltersJson(page: Int, pageSize :Int, types: List[String], compare: String, fieldString: String, operatorString: String, user: String, fieldPicklist: String, operatorPicklist: String, statusList: List[String], fieldSearch: String, operatorSearch: String, stringSearch: String): String =
    s"""{"page":${page},"pageSize":${pageSize},"types":["${types.mkString("\",\"")}"],"filters":{"compare":"${compare}", "children":[{"field":"${fieldString}","operator":"${operatorString}","type":"string","value":"${user}"},{"field":"${fieldPicklist}","operator":"${operatorPicklist}","type":"picklist","value":["${statusList.mkString("\",\"")}"]},{"field":"${fieldSearch}","operator":"${operatorSearch}","type":"string","value":"${stringSearch}"}]}}"""

  def postHistoryNoFilter(requestName: String, page: Int, pageSize: Int, types: List[String]): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/history")
      .body(StringBody(getHistoryQueryNoFilterJson(page, pageSize, types))).asJson
      .check(status is 200))
      .exec { session=> println("POST /services/history API with payload " + getHistoryQueryNoFilterJson(page, pageSize, types))
        session
      }
  }

  def postHistoryFilterString(requestName: String, page: Int, pageSize: Int, types: List[String], compare: String, field: String, operator: String, user: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/history")
      .body(StringBody(getHistoryQueryFilterStringJson(page, pageSize, types, compare, field, operator, user))).asJson
      .check(status is 200))
      .exec { session=> println("POST /services/history API with payload " + getHistoryQueryFilterStringJson(page, pageSize, types, compare, field, operator, user))
        session
      }
  }

  def postHistoryFilterStringAndPickList(requestName: String, page: Int, pageSize: Int, types: List[String], compare: String, fieldString: String, operatorString: String, user: String, fieldPicklist: String, operatorPicklist: String, statusList: List[String]): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/history")
      .body(StringBody(getHistoryQueryFilterStringAndPickListJson(page, pageSize, types, compare, fieldString, operatorString, user, fieldPicklist, operatorPicklist, statusList))).asJson
      .check(status is 200))
      .exec { session=> println("POST /services/history API with payload " + getHistoryQueryFilterStringAndPickListJson(page, pageSize, types, compare, fieldString, operatorString, user, fieldPicklist, operatorPicklist, statusList))
        session
      }
  }

  def postHistoryFilterStringAndString(requestName: String, page: Int, pageSize: Int, types: List[String], compare: String, fieldStringUser: String, operatorStringUser: String, user: String, fieldStringTitle: String, operatorStringTitle: String, query: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/history")
      .body(StringBody(getHistoryQueryFilterStringAndStringJson(page, pageSize, types, compare, fieldStringUser, operatorStringUser, user, fieldStringTitle, operatorStringTitle, query))).asJson
      .check(status is 200))
      .exec { session=> println("POST /services/history API with payload " + getHistoryQueryFilterStringAndStringJson(page, pageSize, types, compare, fieldStringUser, operatorStringUser, user, fieldStringTitle, operatorStringTitle, query))
        session
      }
  }

  def postHistoryWithPrecondition(requestName: String, page: Int, pageSize: Int, types: List[String], compare: String, fieldString: String, operatorString: String, user: String, fieldPicklist: String, operatorPicklist: String, statusList: List[String], fieldSearch: String, operatorSearch: String, stringSearch: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/history")
      .body(StringBody(getHistoryQueryThreeFiltersJson(page, pageSize, types, compare, fieldString, operatorString, user, fieldPicklist, operatorPicklist, statusList, fieldSearch, operatorSearch, stringSearch))).asJson
      .check(status is 200)
      .check(jsonPath("$.data[0][2]").saveAs("martRequestId"))
      .check(jsonPath("$.data[0][6]").saveAs("filteredCount"))
      .check(jsonPath("$.data[0][7]").saveAs("profile"))
    )
  }
}