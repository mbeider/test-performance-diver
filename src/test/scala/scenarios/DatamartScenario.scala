package scenarios

import config.Configuration.userName
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.{HistoryRequest, DatamartRequest}

object DatamartScenario {
  val datamartScenario: ScenarioBuilder =
    scenario("Post View Datamart Precondition")
      .exec(HistoryRequest.postHistoryWithPrecondition("Post History Search with Precondition",1,100,List("single"), "AND",
        "rqst_user_id", "starts_with", userName,
        "stts_cd","eq", List("CMPLT", "PART"),
        "rfrnc_tag_tx", "contain", "Precondition-EQTY-utpt")
      )
      .exec(DatamartRequest.getDatamartStatus("Get Datamart Status","${martRequestId}"))
      .exec(DatamartRequest.postDatamartViewMetadata("Get Datamart View Metadata","${martRequestId}",userName,-10))
      .exec(DatamartRequest.postViewDatamart("Post View Datamart Precondition Materialization","${martRequestId}"))
      .exec(DatamartRequest.postSubsetFieldsViewDatamart("Post View Datamart With Subset Fields Precondition Materialization","${martRequestId}","${profile}"))
      .exec(DatamartRequest.postSortedFieldViewDatamart("Post View Datamart with Sorted Field Precondition Materialization","${martRequestId}","${profile}"))
}
