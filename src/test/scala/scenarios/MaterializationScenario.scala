package scenarios

import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import requests.MaterializationRequest

object MaterializationScenario {
  def materializationScenario(name:String): ScenarioBuilder =
    scenario(name)
      .exec(MaterializationRequest.postMaterializationProcess(name, "EQTY", List("utpt", "fo", "mpq", "utpl", "isg", "cqsl", "eo"), "2020-07-28", "2020-07-28", "10:00:00", "10:00:03", "AAPL","", List("Materialization-Performance")))

  def materializationScenario(name:String, tag:String): ScenarioBuilder =
    scenario(name)
      .exec(MaterializationRequest.postMaterializationProcess(name, "EQTY", List("utpt", "fo", "mpq", "utpl", "isg", "cqsl", "eo"), "2020-07-28", "2020-07-28", "10:00:00", "10:00:03", "AAPL","", List(tag)))

  def materializationEqtySingleSmallScenario(name:String, tag:String): ScenarioBuilder =
    scenario(name)
      .exec(MaterializationRequest.postMaterializationProcess(name, "EQTY", List("qe"), "2018-11-05", "2018-11-05", "11:25:44", "11:26:00", "ADMA","", List(tag)))

  def materializationOptnInterleavedLargeScenario(name:String, tag:String): ScenarioBuilder =
    scenario(name)
      .exec(MaterializationRequest.postMaterializationProcess(name, "OPTN", List("plh"), "2020-07-01", "2020-07-01", "", "", "AAPL","", List(tag)))

  def materializationFixiSingleMonthScenario(name:String, tag:String): ScenarioBuilder =
    scenario(name)
      .exec(MaterializationRequest.postMaterializationProcess(name, "FIXI", List("sptd"), "2014-09-14", "2014-09-14", "00:00:00", "23:59:59", "GNMB3521646","JPMS", List(tag)))

}
