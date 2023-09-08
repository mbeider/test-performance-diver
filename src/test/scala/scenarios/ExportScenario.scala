package scenarios

import config.Configuration.userName
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import requests.ExportRequest
import requests.HistoryRequest

object ExportScenario {
  val postExportProcess: ScenarioBuilder =
    scenario("Export CSV Process")
      .exec(HistoryRequest.postHistoryWithPrecondition("Post History Search with Precondition",1,100,List("single"), "AND",
        "rqst_user_id", "starts_with", userName,
        "stts_cd","eq", List("CMPLT", "PART"),
        "rfrnc_tag_tx", "contain", "Precondition-EQTY-utpt")
      )
      .exec(ExportRequest.exportDatamartToCSV("Export Datamart to Excel file", "${martRequestId}", "${profile}", "${filteredCount}"))
      .exec(ExportRequest.exportDatamartSubsetFieldsToCSV("Export Datamart with Subset Fields to Excel file", "${martRequestId}", "${profile}", "${filteredCount}"))
      .exec(ExportRequest.exportDatamartSortedFieldToCSV("Export Datamart with Sorted Field to Excel file", "${martRequestId}", "${profile}", "${filteredCount}"))
}
