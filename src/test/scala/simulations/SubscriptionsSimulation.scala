package simulations

import config.Configuration._
import io.gatling.core.Predef._
import scenarios.SubscriptionsScenario

import scala.concurrent.duration._
import scala.language.postfixOps

class SubscriptionsSimulation extends Simulation{
  private val subscriptionsScenario = SubscriptionsScenario.subscriptionsScenario
    .inject(rampUsers(1) during(rampup seconds))

  setUp(subscriptionsScenario)
    .protocols(httpProtocol)
    .assertions(forAll.successfulRequests.percent.gte(threshold),
      details("Put View Insert").responseTime.mean.lte(5000),
      details("Share View").responseTime.mean.lte(5000),
      details("Post View in Subscriptions").responseTime.mean.lte(5000),
      details("Get Subscriptions Views").responseTime.mean.lte(5000),
      details("Delete View from Subscriptions").responseTime.mean.lte(5000),
      details("Put View in Subscriptions").responseTime.mean.lte(5000),
      details("Delete View").responseTime.mean.lte(5000))
}
