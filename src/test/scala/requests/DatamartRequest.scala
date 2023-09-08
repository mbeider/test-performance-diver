package requests

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import utilities.GetDataMart.{getFieldFromJson, getLastFromJson, getMartRequestIdFromJson, getProfileFromJson}

object DatamartRequest {

  def getPage(page: String): Int = {
    val jsonPage: Int = page match {
      case "first" => 1
      case "First" => 1
      case "FIRST" => 1
      case "f" => 1
      case "F" => 1
      case "last" => getLastFromJson(columns, records)
      case "Last" => getLastFromJson(columns, records)
      case "LAST" => getLastFromJson(columns, records)
      case "l" => getLastFromJson(columns, records)
      case "L" => getLastFromJson(columns, records)
      case _ => 1
    }
    jsonPage
  }

  def getSort(sortOrder: String): String = {
    val jsonSort: String = sortOrder match {
      case "asc" => "asc"
      case "ASC" => "asc"
      case "a" => "asc"
      case "desc" => "desc"
      case "DESC" => "desc"
      case "d" => "desc"
      case _ => "asc"
    }
    jsonSort
  }

  def getViewDatamartJson(columns: Int, records :Int, page: String, sortOrder: String): String =  s"""{"page":${getPage(page)},"pageSize":1000,"filters":null,"sorts":[{"field": "${getFieldFromJson(columns, records)}","dir": "${getSort(sortOrder)}" }],"profile":${getProfileFromJson(columns, records)},"snapshotTimestamp":"${dateTimeFormat.format(System.currentTimeMillis())}","fields":["*"]}"""

  def postViewDatamart(columns: Int, records: Int, page: String, sortOrder: String): ChainBuilder = {
    exec(http(requestName = s"Post View $columns Columns $records Records $sortOrder Order $page Page")
      .post(baseUrl + "/services/datamart/" + getMartRequestIdFromJson(columns, records))
      .queryParam("cb", System.currentTimeMillis.toString)
      .body(StringBody(getViewDatamartJson(columns, records, page, sortOrder))).asJson
      .check(status is 200))
      .exec { session=> println("Mart ID:" + getMartRequestIdFromJson(columns, records) + " View payload=" + getViewDatamartJson(columns, records, page, sortOrder))
        session
      }
  }

  def getDatamartStatus(requestName: String, martRequestId: String): ChainBuilder = {
    exec(http(requestName)
      .get(baseUrl + "/services/datamart/status")
      .queryParam("martRequestId", s"${martRequestId}")
      .check(status is 200)
      .check(jsonPath("$.martId").saveAs("martId")))
  }

  def postViewDatamart(requestName: String, martRequestId: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + s"/services/datamart/${martRequestId}")
      .queryParam("cb", System.currentTimeMillis.toString)
      .body(StringBody("""{"page":1,"pageSize":1000,"filters":null,"sorts":[],"profile":"${profile}","fields":["*"]}""")).asJson
      .check(status is 200))
      .exec(session => {
        println("Mart ID: " + session("martRequestId").as[String] + " Profile: " + session("profile").as[String])
        session
      })
  }

  def postDatamartViewMetadata(requestName: String, martRequestId: String, userId:String, viewId:Int): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/datamart/view/metadata")
      .body(StringBody(s"""{"martRequestId":"${martRequestId}","userId":"${userId}","viewId":${viewId},"fields":["*"]}""")).asJson
      .check(status is 200))
  }

  def postSubsetFieldsViewDatamart(requestName: String, martRequestId: String, profile: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + s"/services/datamart/${martRequestId}")
      .queryParam("cb", System.currentTimeMillis.toString)
      .body(StringBody(s"""{"page":1,"pageSize":1000,"filters":null,"sorts":[],"profile":"${profile}","fields":["cmn_mkt_class_cd","cmn_bd_nb","cmn_event_tm","utpt_issue_sym_id",
                         |"cmn_firm_mp_id","cmn_rec_type","utpt_rptd_unit_pr","utpt_mkt_cntr_id","cmn_issue_sym_id","cmn_event_dt",
                         |"utpt_msg_seq_nb","cmn_buy_sell_cd","utpt_sllr_sale_day_ct","cmn_event_type_cd","utpt_entry_tm","utpt_orgnl_trade_dt",
                         |"utpt_entry_ts","cmn_event_qt","utpt_rptd_share_qt","utpt_sale_cndtn_cd"]}""".stripMargin)).asJson
      .check(status is 200))
      .exec(session => {
        println("Mart ID: " + session("martRequestId").as[String] + " Profile: " + session("profile").as[String])
        session
      })
  }

  def postSortedFieldViewDatamart(requestName: String, martRequestId: String, profile: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + s"/services/datamart/${martRequestId}")
      .queryParam("cb", System.currentTimeMillis.toString)
      .body(StringBody(s"""{"page":1,"pageSize":1000,"filters":null,"sorts":[{"field":"cmn_event_qt","dir":"asc"}],"profile":"${profile}","fields":["*"]}""")).asJson
      .check(status is 200))
      .exec(session => {
        println("Mart ID: " + session("martRequestId").as[String] + " Profile: " + session("profile").as[String])
        session
      })
  }
}
