package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.PreferencesScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class PreferencesSimulation extends Simulation{
  private val preferencesScenario = PreferencesScenario.getPreferences
    .inject(rampUsers(users) during(rampup seconds))

  setUp(preferencesScenario)
    .protocols(httpProtocol)
    .assertions(forAll.successfulRequests.percent.gte(threshold),
      details("Post History Search with Precondition").responseTime.mean.lte(5000),
      details("Post History Search with Precondition").responseTime.max.lte(7000),
      details("Get Datamart Status").responseTime.mean.lte(5000),
      details("Get Datamart Status").responseTime.max.lte(7000),
      details("Get Preference View of Datamart").responseTime.mean.lte(5000),
      details("Get Preference View of Datamart").responseTime.max.lte(7000),
      details("Put Preference Custom Filter with Datamart").responseTime.mean.lte(5000),
      details("Put Preference Custom Filter with Datamart").responseTime.max.lte(7000),
      details("Put Preference Custom Sort with Datamart").responseTime.mean.lte(5000),
      details("Put Preference Custom Sort with Datamart").responseTime.max.lte(7000),
      details("Put Preference Empty Sort with Datamart").responseTime.mean.lte(5000),
      details("Put Preference Empty Sort with Datamart").responseTime.max.lte(7000),
      details("Put Preference Default Filter with Datamart").responseTime.mean.lte(5000),
      details("Put Preference Default Filter with Datamart").responseTime.max.lte(7000))
}
