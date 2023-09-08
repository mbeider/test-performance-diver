package requests

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object SubscriptionsRequest {

  def postSubscription(requestName: String, userId:String, viewId:String): ChainBuilder = {
    exec(http(requestName)
      .post(baseUrl + "/services/user/" + userId + "/subscriptions/" + viewId)
      .check(status is 204))
  }

  def getSubscription(requestName: String, userId:String): ChainBuilder = {
    exec(http(requestName)
      .get(baseUrl + "/services/user/" + userId + "/subscriptions")
      .queryParam("cb", System.currentTimeMillis.toString)
      .check(status is 200))
  }

  def putSubscription(requestName: String, userId:String, viewIds :List[String]): ChainBuilder = {
    exec(http(requestName)
      .put(baseUrl + "/services/user/" + userId + "/subscriptions")
      .body(StringBody(s"""${viewIds.mkString("[",",","]")}""")).asJson
      .check(status is 204))
  }

  def deleteSubscription(requestName: String, userId:String, viewId:String): ChainBuilder = {
    exec(http(requestName)
      .delete(baseUrl + "/services/user/" + userId + "/subscriptions/" + viewId)
      .check(status is 204))
  }
}
