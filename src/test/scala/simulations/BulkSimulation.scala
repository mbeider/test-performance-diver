package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.BulkScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class BulkSimulation extends Simulation {
  private val postBulkExecution = BulkScenario.postBulkProcess
    .inject(rampUsers(users) during(rampup seconds))

  setUp(postBulkExecution)
    .protocols(httpProtocol)
    .assertions(global.responseTime.max.lt(9500000))
}
