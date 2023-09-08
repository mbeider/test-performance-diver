package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.ViewScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class ViewSimulation extends Simulation{
  private val viewScenario = ViewScenario.viewScenario
    .inject(rampUsers(1) during(rampup seconds))

  setUp(viewScenario)
    .protocols(httpProtocol)
    .assertions(forAll.successfulRequests.percent.gte(threshold),
      details("Put View Insert").responseTime.mean.lte(20000),
      details("Get List of Views").responseTime.mean.lte(20000),
      details("Put Update View").responseTime.mean.lte(20000),
      details("Share View").responseTime.mean.lte(20000),
      details("Delete View").responseTime.mean.lte(20000))
}
