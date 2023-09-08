package simulations

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import scenarios.MaterializationScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class RumpUsersMaterializationSimulation extends Simulation{
  private val rumpUsersMaterializationSimulation = getMaterializationSimulation(materializationScenario, "Rump Users Materialization Scenario")
    .inject(rampUsers(firstSegmentRampUsers).during(firstSegmentDuration seconds), nothingFor(firstSegmentPause),
      rampUsers(secondSegmentRampUsers).during(secondSegmentDuration seconds), nothingFor(secondSegmentPause))

  def getMaterializationSimulation(scenario: String, name: String): ScenarioBuilder = scenario match {
    case "eqtySingleSmall" => MaterializationScenario.materializationEqtySingleSmallScenario(name,"EQTY qe SMALL")
    case "optnInterleavedLarge" => MaterializationScenario.materializationOptnInterleavedLargeScenario(name,"OPTN plh LARGE")
    case "fixiSingleMonth" => MaterializationScenario.materializationFixiSingleMonthScenario(name,"FIXI sptd MONTH")
    case _ => MaterializationScenario.materializationEqtySingleSmallScenario(name,"EQTY qe SMALL")
  }

  setUp(rumpUsersMaterializationSimulation)
    .protocols(httpProtocol)
    .assertions(forAll.failedRequests.percent.lte(threshold),
      details("Rump Users Materialization Scenario").responseTime.mean.lte(30000),
      details("Rump Users Materialization Scenario").responseTime.max.lte(90000))
}
