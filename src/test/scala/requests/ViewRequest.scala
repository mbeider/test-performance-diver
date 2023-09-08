package requests

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.util.Random

object ViewRequest {
  val viewName : String = Random.nextString(7)

  val names: List[String] = List("Rec Type", "Evnt Type", "B/S Cd", "Sym", "Mkt Class", "CRD", "IMID/MPID", "Evnt Dt", "Evnt Tm", "Evnt Qt",
    "Trd Dt", "Entry Tm", "Entry Ts", "Sym", "Rptd Share Qt", "Rptd Unit Pr", "Mkt Cntr", "Sale Cndtn Cd", "Sllr Sale Day Ct", "Msg Seq Nb", "Src Line Cd",
    "Chng Ind Cd", "Msg Ctgry Cd", "Msg Type Cd", "Rpt Sub Type Cd", "Cnsld Lsale Pr", "Lsale Mkr Cntr Cd", "File Src Cd", "Sub Mkt Cntr Cd", "Trd Thru Exmpt Cd")

  val updatedNames: List[String] = List("Rec Type", "Evnt Type", "B/S Cd", "Sym", "Mkt Class", "CRD", "IMID/MPID", "Evnt Dt", "Evnt Tm", "Evnt Qt",
    "Ttl Vol Qt", "Cnsld High Pr", "Cnsld Low Pr", "Was Trd Thru Exmpt Cd", "WAS Rptd Share Qt", "WAS Rptd Unit Pr", "WAS Sale Cndtn Cd", "WAS Sllr Sale Day Ct", "WAS Msg Seq Nb",
    "Prtcp Event 1 Tm", "Prtcp Event 2 Tm", "SIP Trans ID", "Rec Load Dt", "Drvd Final Rptd Unit Pr", "Drvd IIV Rptd Unit Pr", "Drvd Final WAS Rptd Unit Pr",
    "Drvd IIV WAS Rptd Unit Pr", "Drvd Final Cnsld High Pr", "Drvd Final Cnsld Low Pr", "Drvd Final Cnsld Lsale Pr", "ETMF Ind")

  val ids: List[String] = List("cmn_rec_type", "cmn_event_type_cd", "cmn_buy_sell_cd", "cmn_issue_sym_id", "cmn_mkt_class_cd", "cmn_bd_nb", "cmn_firm_mp_id",
    "cmn_event_dt", "cmn_event_tm", "cmn_event_qt", "utpt_orgnl_trade_dt", "utpt_entry_tm", "utpt_entry_ts", "utpt_issue_sym_id", "utpt_rptd_share_qt", "utpt_rptd_unit_pr",
    "utpt_mkt_cntr_id", "utpt_sale_cndtn_cd", "utpt_sllr_sale_day_ct", "utpt_msg_seq_nb", "utpt_src_line_cd", "utpt_chng_ind_cd", "utpt_msg_ctgry_cd", "utpt_msg_type_cd",
    "utpt_rpt_sub_type_cd", "utpt_cnsld_lsale_pr", "utpt_lsale_mkt_cntr_cd", "utpt_file_src_cd", "utpt_sub_mkt_cntr_cd", "utpt_trd_thru_exmpt_cd")

  val updatedIds: List[String] = List("cmn_rec_type", "cmn_event_type_cd", "cmn_buy_sell_cd", "cmn_issue_sym_id", "cmn_mkt_class_cd", "cmn_bd_nb", "cmn_firm_mp_id",
    "cmn_event_dt", "cmn_event_tm", "cmn_event_qt", "utpt_ttl_vol_qt", "utpt_cnsld_high_pr", "utpt_cnsld_low_pr", "utpt_was_trd_thru_exmpt_cd", "utpt_was_rptd_share_qt",
    "utpt_was_rptd_unit_pr", "utpt_was_sale_cndtn_cd", "utpt_was_sllr_sale_day_ct", "utpt_was_msg_seq_nb", "utpt_prtcp_event_1_tm", "utpt_prtcp_event_2_tm",
    "utpt_sip_trans_id", "utpt_rec_load_dt", "utpt_drvd_final_rptd_unit_pr", "utpt_drvd_iiv_rptd_unit_pr", "utpt_drvd_final_was_rptd_unit_pr", "utpt_drvd_iiv_was_rptd_unit_pr",
    "utpt_drvd_final_cnsld_high_pr", "utpt_drvd_final_cnsld_low_pr", "utpt_drvd_final_cnsld_lsale_pr", "utpt_etmf_ind")

  val descriptions: List[String] = List("Record Type", "Event Type", "Buy Sell Code", "Issue Symbol ID", "Market Class Code", "CRD Number", "IMID/MPID",
    "Event Date", "Event Time", "Event Quantity", "Trade Date", "Entry Time", "Entry Timestamp", "Issue Symbol Id", "Reported Share Quantity", "Reported Unit Price",
    "Market Center ID", "Sale Condition Code", "Seller Sale Day Count", "Message Sequence Number", "Source Line Code", "Change Indicator Code", "Message Category Code",
    "Message Type Code", "Report Sub Type Code", "Consolidated Last Sale Price", "Last Sale Market Center Code", "File Source Code", "Sub Market Center Code",
    "Trade Through Exempt Code")

  val updatedDescriptions: List[String] = List("Record Type", "Event Type", "Buy Sell Code", "Issue Symbol ID", "Market Class Code", "CRD Number", "IMID/MPID",
    "Event Date", "Event Time", "Event Quantity", "Total Volume Quantity", "Consolidated High Price", "Consolidated Low Price", "WAS Trade Exempt Code", "WAS Reported Share Quantity",
    "WAS Reported Unit Price", "WAS Sale Condition Code", "WAS Seller Sale Day Count", "WAS Message Sequence Number", "Participant Event Time 1", "Participant Event Time 2",
    "SIP Transaction ID", "Record Load Date", "Derived Final Reported Unit Pr", "Derived IIV Reported Unit Price", "Derived Final WAS Reported Unit Price",
    "Derived IIV WAS Reported Unit Pr", "Derived Final Consolidated High Price", "Derived Final Consolidated Low Price", "Derived Final Consolidated Last Sale Price", "ETMF Indicator")

  val recordTypes: List[String] = List("Common", "Common", "Common", "Common", "Common", "Common", "Common", "Common", "Common", "Common",
    "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades",
    "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades",
    "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades")

  val updatedRecordTypes: List[String] = List("Common", "Common", "Common", "Common", "Common", "Common", "Common", "Common", "Common", "Common",
    "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades",
    "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades",
    "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades", "SIP UTP Trades")

  val displayFormats: List[String] = List("string", "string", "string", "string", "string", "string", "string", "date", "time_nano", "integer",
    "date", "time_nano", "timestamp_nano", "string", "integer", "decimal_large", "string", "string", "integer", "string", "string", "string", "string",
    "string", "string", "decimal_large", "string", "string", "string", "string")

  val updatedDisplayFormats: List[String] = List("string", "string", "string", "string", "string", "string", "string", "date", "time_nano", "integer",
    "integer", "decimal_large", "decimal_large", "string", "integer", "decimal_large", "string", "integer", "integer", "time_nano", "time_nano", "string", "date", "decimal_large",
    "decimal_large", "decimal_large", "decimal_large", "decimal_large", "decimal_large", "decimal_large", "string")

  def putViewInsert(requestName: String, owner:String, isPublic:String, isActive:String, columns : String): ChainBuilder = {
    exec(http(requestName)
      .put(baseUrl + "/services/view/")
      .body(StringBody(s"""{"name":"${viewName}","owner":"${owner}","isPublic":"${isPublic}","isActive":"${isActive}","columns":$columns}""")).asJson
      .check(status is 200)
      .check(bodyString.saveAs( "viewId" )))
      .exec(session => {
        println("Insert View ID: " + session("viewId").as[String])
        session
      })
  }

  def putViewUpdate(requestName: String, id:String, owner:String, isPublic:String, isActive:String, columns : String): ChainBuilder = {
    exec(http(requestName)
      .put(baseUrl + "/services/view/")
      .body(StringBody(s"""{"id":"${id}","name":"${viewName}","owner":"${owner}","isPublic":"${isPublic}","isActive":"${isActive}","columns":$columns,"updatedDate":"${dateFormat.format(System.currentTimeMillis())}"}""")).asJson
      .check(status is 200)
      .check(bodyString.saveAs( "viewId" )))
      .exec(session => {
        println("Update View ID: " + session("viewId").as[String])
        session
      })
  }

  def getViews(requestName: String): ChainBuilder = {
    exec(http(requestName)
      .get(baseUrl + "/services/view")
      .queryParam("cb", System.currentTimeMillis.toString)
      .check(status is 200))
  }

  def shareView(requestName: String, id:String, isPublic:String): ChainBuilder = {
    exec(http(requestName)
      .put(baseUrl + "/services/view/" + id + "/share")
      .body(StringBody(s"""{"isPublic":"${isPublic}"}""")).asJson
      .check(status is 204))
  }

  def deleteView(requestName: String, id:String): ChainBuilder = {
    exec(http(requestName)
      .delete(baseUrl + "/services/view/"+ id)
      .check(status is 204))
  }
}
