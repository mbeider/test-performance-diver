package scenarios

import config.Configuration.userName
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.{DatamartRequest, HistoryRequest, PreferencesRequest}

object PreferencesScenario {
  val getPreferences: ScenarioBuilder =
    scenario("Get Preference of Datamart")
      .exec(HistoryRequest.postHistoryWithPrecondition("Post History Search with Precondition",1,100,List("single"), "AND",
        "rqst_user_id", "starts_with", userName,
        "stts_cd","eq", List("CMPLT", "PART"),
        "rfrnc_tag_tx", "contain", "Precondition-EQTY-utpt"))
      .exec(DatamartRequest.getDatamartStatus("Get Datamart Status", "${martRequestId}"))
      .exec(PreferencesRequest.getPreference("Get Preference View of Datamart", "${martId}", userName, -10.toLong))
      .exec(PreferencesRequest.putPreferenceFilters("Put Preference Custom Filter with Datamart", "${martId}", userName, -10.toLong, PreferencesRequest.pickListAndStringFilterJson("and", "cmn_rec_type", List("SIP UTP Trades"), "eq", "utpt_issue_sym_id", "AAPL", "contain")))
      .exec(PreferencesRequest.putPreferenceSorts("Put Preference Custom Sort with Datamart", "${martId}", userName, -10.toLong, PreferencesRequest.sortJson("cmn_event_qt", "asc")))
      .exec(PreferencesRequest.putPreferenceSorts("Put Preference Empty Sort with Datamart", "${martId}", userName, -10.toLong, PreferencesRequest.emptySortJson()))
      .exec(PreferencesRequest.putPreferenceFilters("Put Preference Default Filter with Datamart", "${martId}", userName, -10.toLong, PreferencesRequest.pickListFilterJson("and", "cmn_rec_type", List("SIP UTP Trades"), "eq")))
      .exec(PreferencesRequest.getPreference("Get Preference View of Datamart", "${martId}", userName, -10.toLong))
}
