package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.AggregatorScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class AggregatorSimulation extends Simulation{
  private val aggregatorScenario = AggregatorScenario.aggregatorScenario
    .inject(rampUsers(users) during(rampup seconds))

  setUp(aggregatorScenario)
    .protocols(httpProtocol)
    .assertions(forAll.successfulRequests.percent.gte(threshold),
      details("Post History Search with Precondition").responseTime.mean.lte(30000),
      details("Post History Search with Precondition").responseTime.max.lte(90000),
      details("Post Aggregator Summarize").responseTime.mean.lte(30000),
      details("Post Aggregator Summarize").responseTime.max.lte(90000),
      details("Post Aggregator Download").responseTime.mean.lte(30000),
      details("Post Aggregator Download").responseTime.max.lte(90000)
    )
}
