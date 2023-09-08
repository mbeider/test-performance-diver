package simulations

import config.Configuration
import config.Configuration._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import scenarios.MaterializationScenario
import scala.concurrent.duration._
import scala.language.postfixOps

class MaterializationSimulation extends Simulation {
  private val materializationSimulation = getMaterializationSimulation(materializationScenario)
    .inject(rampUsers(Configuration.users) during(Configuration.rampup seconds))

  setUp(materializationSimulation).protocols(httpProtocol)
    .assertions(forAll.failedRequests.percent.lte(threshold),
      details(materializationScenario).responseTime.mean.lte(20000))

  def getMaterializationSimulation(scenario: String): ScenarioBuilder = scenario match {
    case "eqtySingleSmall" => MaterializationScenario.materializationEqtySingleSmallScenario("eqtySingleSmall","EQTY qe SMALL")
    case "optnInterleavedLarge" => MaterializationScenario.materializationOptnInterleavedLargeScenario("optnInterleavedLarge","OPTN plh LARGE")
    case "fixiSingleMonth" => MaterializationScenario.materializationFixiSingleMonthScenario("fixiSingleMonth","FIXI sptd MONTH")
    case _ => MaterializationScenario.materializationEqtySingleSmallScenario("eqtySingleSmall","EQTY qe SMALL")
  }
}
