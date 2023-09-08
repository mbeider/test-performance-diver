package scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import requests.BulkRequest

object BulkScenario {
  val postBulkProcess: ScenarioBuilder =
    scenario("Bulk Process")
      .exec(BulkRequest.postBulkInitiate)
}
