package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.DatamartScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class DatamartSimulation extends Simulation{
  private val datamartScenario = DatamartScenario.datamartScenario
    .inject(rampUsers(users) during(rampup seconds))

  setUp(datamartScenario)
    .protocols(httpProtocol)
    .assertions(forAll.successfulRequests.percent.gte(threshold),
      details("Post History Search with Precondition").responseTime.mean.lte(30000),
      details("Post History Search with Precondition").responseTime.max.lte(90000),
      details("Get Datamart Status").responseTime.mean.lte(30000),
      details("Get Datamart Status").responseTime.max.lte(90000),
      details("Get Datamart View Metadata").responseTime.mean.lte(60000),
      details("Get Datamart View Metadata").responseTime.max.lte(90000),
      details("Post View Datamart Precondition Materialization").responseTime.mean.lte(30000),
      details("Post View Datamart Precondition Materialization").responseTime.max.lte(90000),
      details("Post View Datamart With Subset Fields Precondition Materialization").responseTime.mean.lte(30000),
      details("Post View Datamart With Subset Fields Precondition Materialization").responseTime.max.lte(90000),
      details("Post View Datamart with Sorted Field Precondition Materialization").responseTime.mean.lte(40000),
      details("Post View Datamart with Sorted Field Precondition Materialization").responseTime.max.lte(90000))
}
