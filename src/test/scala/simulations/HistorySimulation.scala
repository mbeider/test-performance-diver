package simulations

import config.Configuration
import config.Configuration._
import io.gatling.core.Predef._
import scenarios.HistoryScenario
import scala.concurrent.duration._
import scala.language.postfixOps

class HistorySimulation extends Simulation{
    private val allHistoryScenarios = HistoryScenario.allHistoryScenario()
      .inject(rampUsers(Configuration.users) during(Configuration.rampup seconds))

  setUp(allHistoryScenarios)
    .protocols(httpProtocol)
    .assertions(forAll.successfulRequests.percent.gte(threshold),
      details("Post History Search with filter \"Everyone\"").responseTime.mean.lte(20000),
      details("Post History Search with filter \"Belongs to me\"").responseTime.mean.lte(20000),
      details("Post History Search with filter \"By user\"").responseTime.mean.lte(20000),
      details("Post History Search with status \"Completed\"").responseTime.mean.lte(20000),
      details("Post History Search with status \"Error\"").responseTime.mean.lte(20000),
      details("Post History Search with status \"Expired\"").responseTime.mean.lte(20000),
      details("Post History Search with status \"In Progress\"").responseTime.mean.lte(20000),
      details("Post History Search with request type \"Single Requests\"").responseTime.mean.lte(20000),
      details("Post History Search with request type \"Bulk Requests\"").responseTime.mean.lte(20000),
      details("Post History Search that contains \"Title\"").responseTime.mean.lte(20000)
    )
}
