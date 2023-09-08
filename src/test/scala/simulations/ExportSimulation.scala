package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.ExportScenario
import scala.concurrent.duration._
import scala.language.postfixOps

class ExportSimulation extends Simulation {
  private val postExportExecution = ExportScenario.postExportProcess
    .inject(rampUsers(users) during(rampup seconds))

  setUp(postExportExecution)
    .protocols(httpProtocol)
    .assertions(global.responseTime.max.lt(1500000),
      details("Export Datamart to Excel file").responseTime.mean.lte(30000),
      details("Export Datamart to Excel file").responseTime.max.lte(90000),
      details("Export Datamart with Subset Fields to Excel file").responseTime.mean.lte(30000),
      details("Export Datamart with Subset Fields to Excel file").responseTime.max.lte(90000),
      details("Export Datamart with Sorted Field to Excel file").responseTime.mean.lte(30000),
      details("Export Datamart with Sorted Field to Excel file").responseTime.max.lte(90000))
}
