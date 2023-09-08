package requests

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object BulkRequest {

  val postBulkJson: String = s"""{"contentType": "excel", "artifactName": "User_1_Presto_30_Bulk_Test","fields": [],"requests": [{"recordTypes": ["oet"],"fromDate": "2015-08-24","toDate": "2015-08-24","fromTime": "00:00:00","toTime": "23:59:59","issueSym": "celg","issueId": "","firmMpId": "NITE","allRelatedFirms": true,"useOatsProcessingDate": false,"preFilters": null,"tags": "relathena-001"}]}"""

  val postBulkInitiate: ChainBuilder = {
    group("Post Bulk Process") {
      exec(http(requestName = "Initiate Bulk Request")
        .post("/bulk/initiate")
        .body(RawFileBody("./src/test/resources/bodies/bulk03.json")).asJson
        .check(status is 200)
        .check(jsonPath("$.bulkRequestId").saveAs("bulkRequestId"))
        .check(jsonPath("$.statusCode").saveAs("statusCode")))
        .exec { session=>
          println("Bulk Request ID :" + session("bulkRequestId").as[String])
          session.set("statusCode","PRGRS")
        }
        .asLongAs(session => session("statusCode").as[String] != "CMPLT", exitASAP=true) {
          exec(http(requestName = "Pull Bulk Process Status")
            .post("/services/statuses")
            .body(StringBody("{\"martRequestIds\":[],\"bulkRequestIds\":[\"${bulkRequestId}\"]}")).asJson
            .check(status is 200)
            .check(jsonPath("$.bulkRequestStatuses[0].bulkRequestTitle").saveAs("bulkRequestTitle"))
            .check(jsonPath("$.bulkRequestStatuses[0].bulkRequestId").saveAs("bulkRequestId"))
            .check(jsonPath("$.bulkRequestStatuses[0].totalRequests").saveAs("totalRequests"))
            .check(jsonPath("$.bulkRequestStatuses[0].statusCode").saveAs("statusCode"))
            .check(jsonPath("$.bulkRequestStatuses[0].numberOfFiles").saveAs("numberOfFiles"))
            .check(jsonPath("$.bulkRequestStatuses[0].fileSize").saveAs("fileSize"))
            .check(jsonPath("$.bulkRequestStatuses[0].oversized").saveAs("oversized")))
            .exec(session => {
              val newSession = session.set("statusCode", session("statusCode"))
              println("Status Code: " + session("statusCode").as[String] + ", Bulk Request ID: " + session("bulkRequestId").as[String] + " Bulk Title: " + session("bulkRequestTitle").as[String] + " of Total Requests: " + session("totalRequests").as[String] )
              newSession
            })
            .pause(30)
        }
    }
  }
}
