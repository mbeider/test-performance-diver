package scenarios

import config.Configuration.userName
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.ViewRequest
import requests.ViewRequest.{descriptions, displayFormats, ids, names, recordTypes, updatedDescriptions, updatedDisplayFormats, updatedIds, updatedNames, updatedRecordTypes}

object ViewScenario {

  val columns = List(names, ids, descriptions, recordTypes, displayFormats).transpose.map { case name :: id :: description :: recordType :: displayFormat ::_ =>
    raw"""{ "name": "$name", "id": "$id", "description": "$description", "recordType": "$recordType", "displayFormat": "$displayFormat"}"""}.mkString("[", ",", "]")

  val updatedColumns = List(updatedNames, updatedIds, updatedDescriptions, updatedRecordTypes, updatedDisplayFormats).transpose.map { case updatedName :: updatedId :: updatedDescription :: updatedRecordType :: updatedDisplayFormat ::_ =>
    raw"""{ "name": "$updatedName", "id": "$updatedId", "description": "$updatedDescription", "recordType": "$updatedRecordType", "displayFormat": "$updatedDisplayFormat"}"""}.mkString("[", ",", "]")

  val viewScenario: ScenarioBuilder =
    scenario("Put and Delete View")
      .exec(ViewRequest.putViewInsert("Put View Insert", userName, "N", "Y", columns))
      .exec(ViewRequest.getViews("Get List of Views"))
      .exec(ViewRequest.putViewUpdate("Put Update View", "${viewId}",userName, "N", "Y", updatedColumns))
      .exec(ViewRequest.shareView("Share View", "${viewId}","Y"))
      .exec(ViewRequest.deleteView("Delete View","${viewId}"))
}
