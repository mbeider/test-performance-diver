package requests

import config.Configuration.{baseUrl, headers_download_API}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object AggregatorRequest {
  val postAggregatorSummarize: String = s"""{"pageSize":100,"page":0,"measures":[{"function":"count","alias":"*","key":"*"}],
      |"filters":{"compare":"AND","children":[{"compare":"OR","children":[{"compare":"AND","children":[{"field":"cmn_rec_type","type":"picklist","value":["SIP UTP Trades"],"operator":"eq"},
      |{"compare":"AND","children":[{"field":"utpt_rptd_share_qt","type":"number","value":["10"],"operator":"gt"}]}]}]}]},
      |"dimensions":[{"key":"cmn_rec_type"},{"key":"cmn_event_type_cd"},{"key":"cmn_buy_sell_cd"},{"key":"cmn_issue_sym_id"},
      |{"key":"cmn_bd_nb"},{"key":"cmn_firm_mp_id"},{"key":"utpt_orgnl_trade_dt"},{"key":"utpt_rptd_share_qt"},
      |{"key":"utpt_rptd_unit_pr"},{"key":"utpt_mkt_cntr_id"}]}""".stripMargin

  val postAggregatorDownload: String =s"""{"pageSize":3000,"page":0,"measures":[{"function":"count","alias":"Record Count","key":"*"}],
       |"filters":{"compare":"AND","children":[{"compare":"OR","children":[{"compare":"AND","children":[{"field":"cmn_rec_type","type":"picklist","value":["SIP UTP Trades"],"operator":"eq"},
       |{"compare":"AND","children":[{"field":"utpt_rptd_share_qt","type":"number","value":["10"],"operator":"gt"}]}]}]}]},
       |"dimensions":[{"alias":"Rec Type","key":"cmn_rec_type"},{"alias":"Evnt Type","key":"cmn_event_type_cd"},
       |{"alias":"B/S Cd","key":"cmn_buy_sell_cd"},{"alias":"Sym","key":"cmn_issue_sym_id"},{"alias":"CRD","key":"cmn_bd_nb"},
       |{"alias":"IMID/MPID","key":"cmn_firm_mp_id"},{"alias":"Trd Dt","key":"utpt_orgnl_trade_dt"},{"alias":"Rptd Share Qt","key":"utpt_rptd_share_qt"},
       |{"alias":"Rptd Unit Pr","key":"utpt_rptd_unit_pr"},{"alias":"Mkt Cntr","key":"utpt_mkt_cntr_id"}]}""".stripMargin

  def postAggregatorSummarize(requestName: String, martRequestId: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + s"/aggregator/summarize/${martRequestId}")
      .body(StringBody(postAggregatorSummarize))
      .check(status is 200))
      .exec { session=> println("POST /aggregator/summarize/ API with payload " + postAggregatorSummarize)
        session
      }
  }

  def postAggregatorDownload(requestName: String, martRequestId: String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + s"/aggregator/download/${martRequestId}")
      .headers(headers_download_API)
      .body(StringBody(postAggregatorDownload))
      .check(status is 200))
      .exec { session=> println("POST /aggregator/download/ API with payload " + postAggregatorDownload)
        session
      }
  }
}
