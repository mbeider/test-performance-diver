package simulations

import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import scenarios.MaterializationScenario

import scala.concurrent.duration.DurationDouble
import scala.language.postfixOps

class ConstantRateMaterializationSimulation extends Simulation{
  private val constantRateMaterializationSimulation = getMaterializationSimulation(materializationScenario,"Constant Rate Materialization Scenario")
    .inject(constantUsersPerSec(firstSegmentUsersPerSecond).during(firstSegmentDuration seconds), nothingFor(firstSegmentPause),
      constantUsersPerSec(secondSegmentUsersPerSecond).during(secondSegmentDuration seconds), nothingFor(secondSegmentPause))

  def getMaterializationSimulation(scenario: String, name: String): ScenarioBuilder = scenario match {
    case "eqtySingleSmall" => MaterializationScenario.materializationEqtySingleSmallScenario(name,"EQTY qe SMALL")
    case "optnInterleavedLarge" => MaterializationScenario.materializationOptnInterleavedLargeScenario(name,"OPTN plh LARGE")
    case "fixiSingleMonth" => MaterializationScenario.materializationFixiSingleMonthScenario(name,"FIXI sptd MONTH")
    case _ => MaterializationScenario.materializationEqtySingleSmallScenario(name,"EQTY qe SMALL")
  }

  setUp(constantRateMaterializationSimulation)
    .protocols(httpProtocol)
    .assertions(forAll.failedRequests.percent.lte(threshold),
      details("Constant Rate Materialization Scenario").responseTime.mean.lte(30000),
      details("Constant Rate Materialization Scenario").responseTime.max.lte(90000))
}
