package scenarios

import config.Configuration.userName
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.{AggregatorRequest, HistoryRequest}

object AggregatorScenario {
  val aggregatorScenario: ScenarioBuilder =
    scenario("Post View Precondition")
      .exec(HistoryRequest.postHistoryWithPrecondition("Post History Search with Precondition",1,100,List("single"), "AND",
        "rqst_user_id", "starts_with", userName,
        "stts_cd","eq", List("CMPLT", "PART"),
        "rfrnc_tag_tx", "contain", "Precondition-EQTY-utpt")
      )
      .exec(AggregatorRequest.postAggregatorSummarize("Post Aggregator Summarize","${martRequestId}"))
      .exec(AggregatorRequest.postAggregatorSummarize("Post Aggregator Download","${martRequestId}"))
}
