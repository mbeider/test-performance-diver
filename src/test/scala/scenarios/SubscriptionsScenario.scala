package scenarios

import config.Configuration.userName
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.{SubscriptionsRequest, ViewRequest}
import requests.ViewRequest._

object SubscriptionsScenario {

  val columns = List(names, ids, descriptions, recordTypes, displayFormats).transpose.map { case name :: id :: description :: recordType :: displayFormat ::_ =>
    raw"""{ "name": "$name", "id": "$id", "description": "$description", "recordType": "$recordType", "displayFormat": "$displayFormat"}"""}.mkString("[", ",", "]")

  val subscriptionsScenario: ScenarioBuilder =
    scenario("Add and Remove View in Subscriptions")
      .exec(ViewRequest.putViewInsert("Put View Insert", userName, "N", "Y", columns))
      .exec(ViewRequest.shareView("Share View", "${viewId}","Y"))
      .exec(SubscriptionsRequest.postSubscription("Post View in Subscriptions",userName, "${viewId}"))
      .exec(SubscriptionsRequest.getSubscription("Get Subscriptions Views",userName))
      .exec(SubscriptionsRequest.deleteSubscription("Delete View from Subscriptions",userName, "${viewId}"))
      .exec(SubscriptionsRequest.getSubscription("Get Subscriptions Views",userName))
      .exec(SubscriptionsRequest.putSubscription("Put View in Subscriptions",userName, List("${viewId}")))
      .exec(SubscriptionsRequest.getSubscription("Get Subscriptions Views",userName))
      .exec(SubscriptionsRequest.deleteSubscription("Delete View from Subscriptions",userName, "${viewId}"))
      .exec(SubscriptionsRequest.getSubscription("Get Subscriptions Views",userName))
      .exec(ViewRequest.shareView("Share View", "${viewId}","N"))
      .exec(ViewRequest.deleteView("Delete View","${viewId}"))
}
