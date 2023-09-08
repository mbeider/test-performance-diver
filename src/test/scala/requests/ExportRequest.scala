package requests

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object ExportRequest {
  def exportDatamartToCSV(name: String, martRequestId: String, profile: String, filteredCount: String): ChainBuilder = {
    group(name) {
      exec(http(requestName = "Initiate Export")
        .post(s"/export/datamart/${martRequestId}")
        .body(StringBody(s"""{"martRequestId":"${martRequestId}","profile":"${profile}","fields":["*"],"filteredCount":"${filteredCount}","sorts":[],"includeCCID": false}"""))
        .check(status is 200)
        .check(jsonPath("$.exportId").saveAs("exportID")))
        .exec { session=>
          println("Export ID :" + session("exportID").as[String])
          session.set("statusCode","QUEUED")
        }
        .asLongAs(session => session("statusCode").as[String] != "EXPORT_COMPLETE", exitASAP=true) {
          exec(http(requestName = "Poll Export Status")
            .get("/export/${exportID}/status")
            .queryParam("cb", System.currentTimeMillis.toString)
            .check(status is 200)
            .check(jsonPath("$.statusCode").saveAs("statusCode"))
            .check(jsonPath("$.rowsProcessed").saveAs("rowsProcessed"))
            .check(jsonPath("$.totalRows").saveAs("totalRows")))
            .exec(session => {
              val newSession = session.set("statusCode", session("statusCode"))
              println("Mart ID: " + session("martRequestId").as[String] + " Export ID: " + session("exportID").as[String] + " Status Code: " + session("statusCode").as[String] + " Processed " + session("rowsProcessed").as[String] + " of " +  session("totalRows").as[String] + " rows")
              newSession
            })
            .pause(2)
        }
    }
  }

  def exportDatamartSubsetFieldsToCSV(name: String, martRequestId: String, profile: String, filteredCount: String): ChainBuilder = {
    group(name) {
      exec(http(requestName = "Initiate Export")
        .post(s"/export/datamart/${martRequestId}")
        .body(StringBody(s"""{"martRequestId":"${martRequestId}","profile":"${profile}","fields":["cmn_mkt_class_cd","cmn_bd_nb","cmn_event_tm","utpt_issue_sym_id",
                           |"cmn_firm_mp_id","cmn_rec_type","utpt_rptd_unit_pr","utpt_mkt_cntr_id","cmn_issue_sym_id","cmn_event_dt",
                           |"utpt_msg_seq_nb","cmn_buy_sell_cd","utpt_sllr_sale_day_ct","cmn_event_type_cd","utpt_entry_tm","utpt_orgnl_trade_dt",
                           |"utpt_entry_ts","cmn_event_qt","utpt_rptd_share_qt","utpt_sale_cndtn_cd"],"filteredCount":"${filteredCount}","sorts":[],"includeCCID": false}""".stripMargin))
        .check(status is 200)
        .check(jsonPath("$.exportId").saveAs("exportID")))
        .exec { session=>
          println("Export ID :" + session("exportID").as[String])
          session.set("statusCode","QUEUED")
        }
        .asLongAs(session => session("statusCode").as[String] != "EXPORT_COMPLETE", exitASAP=true) {
          exec(http(requestName = "Poll Export Status")
            .get("/export/${exportID}/status")
            .queryParam("cb", System.currentTimeMillis.toString)
            .check(status is 200)
            .check(jsonPath("$.statusCode").saveAs("statusCode"))
            .check(jsonPath("$.rowsProcessed").saveAs("rowsProcessed"))
            .check(jsonPath("$.totalRows").saveAs("totalRows")))
            .exec(session => {
              val newSession = session.set("statusCode", session("statusCode"))
              println("Mart ID: " + session("martRequestId").as[String] + " Export ID: " + session("exportID").as[String] + " Status Code: " + session("statusCode").as[String] + " Processed " + session("rowsProcessed").as[String] + " of " +  session("totalRows").as[String] + " rows")
              newSession
            })
            .pause(2)
        }
    }
  }

  def exportDatamartSortedFieldToCSV(name: String, martRequestId: String, profile: String, filteredCount: String): ChainBuilder = {
    group(name) {
      exec(http(requestName = "Initiate Export")
        .post(s"/export/datamart/${martRequestId}")
        .body(StringBody(s"""{"martRequestId":"${martRequestId}","profile":"${profile}","fields":["*"],"filteredCount":"${filteredCount}","sorts":[{"field":"cmn_event_qt","dir":"asc"}],"includeCCID": false}"""))
        .check(status is 200)
        .check(jsonPath("$.exportId").saveAs("exportID")))
        .exec { session=>
          println("Export ID :" + session("exportID").as[String])
          session.set("statusCode","QUEUED")
        }
        .asLongAs(session => session("statusCode").as[String] != "EXPORT_COMPLETE", exitASAP=true) {
          exec(http(requestName = "Poll Export Status")
            .get("/export/${exportID}/status")
            .queryParam("cb", System.currentTimeMillis.toString)
            .check(status is 200)
            .check(jsonPath("$.statusCode").saveAs("statusCode"))
            .check(jsonPath("$.rowsProcessed").saveAs("rowsProcessed"))
            .check(jsonPath("$.totalRows").saveAs("totalRows")))
            .exec(session => {
              val newSession = session.set("statusCode", session("statusCode"))
              println("Mart ID: " + session("martRequestId").as[String] + " Export ID: " + session("exportID").as[String] + " Status Code: " + session("statusCode").as[String] + " Processed " + session("rowsProcessed").as[String] + " of " +  session("totalRows").as[String] + " rows")
              newSession
            })
            .pause(2)
        }
    }
  }
}
