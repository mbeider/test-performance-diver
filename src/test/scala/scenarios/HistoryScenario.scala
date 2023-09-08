package scenarios

import config.Configuration.userName
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.HistoryRequest

object HistoryScenario {
  def allHistoryScenario(): ScenarioBuilder =
    scenario("All Scenarios of History microservice endpoints")
      .exec(HistoryRequest.postHistoryNoFilter("Post History Search with filter \"Everyone\"",1,100,List("single", "bulk")))
      .exec(HistoryRequest.postHistoryFilterString("Post History Search with filter \"Belongs to me\"",1,100,List("single", "bulk"),"AND","rqst_user_id", "starts_with", userName))
      .exec(HistoryRequest.postHistoryFilterString("Post History Search with filter \"By user\"", 1,100,List("single", "bulk"), "AND","rqst_user_id", "starts_with","30075"))
      .exec(HistoryRequest.postHistoryFilterStringAndPickList("Post History Search with status \"Completed\"", 1,100,List("single", "bulk"), "AND","rqst_user_id", "starts_with", userName, "stts_cd","eq", List("CMPLT", "PART")))
      .exec(HistoryRequest.postHistoryFilterStringAndPickList("Post History Search with status \"Error\"", 1,100,List("single", "bulk"), "AND","rqst_user_id", "starts_with", userName, "stts_cd", "eq",List("ERR", "RJCTD")))
      .exec(HistoryRequest.postHistoryFilterStringAndPickList("Post History Search with status \"Expired\"", 1,100,List("single", "bulk"), "AND","rqst_user_id", "starts_with", userName, "stts_cd", "eq",List("XPRG", "XPRD")))
      .exec(HistoryRequest.postHistoryFilterStringAndPickList("Post History Search with status \"In Progress\"", 1,100,List("single", "bulk"), "AND","rqst_user_id","starts_with", userName, "stts_cd", "eq",List("PRGRS", "STGNG", "LDNG", "QUEUE")))
      .exec(HistoryRequest.postHistoryFilterString("Post History Search with request type \"Single Requests\"", 1,100,List("single"), "AND","rqst_user_id", "starts_with", userName))
      .exec(HistoryRequest.postHistoryFilterString("Post History Search with request type \"Bulk Requests\"", 1,100,List("bulk"), "AND","rqst_user_id", "starts_with",userName))
      .exec(HistoryRequest.postHistoryFilterStringAndString("Post History Search that contains \"Title\"", 1,100,List("single", "bulk"), "AND","rqst_user_id", "starts_with", userName, "rfrnc_tag_tx", "contain","e"))
}