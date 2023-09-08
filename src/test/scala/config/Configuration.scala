package config

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import java.text.SimpleDateFormat

object Configuration {
  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  /* Default Environment variables */
  val baseUrl: String = getProperty("baseUrl", "https://diver-ecs-sbint.diver.dev.finra.org")
  val users: Int = getProperty("users", "2").toInt
  val threshold: Int = getProperty("threshold", "90").toInt
  val rampup: Int = getProperty("rampup", "60").toInt
  val throughput: Int = getProperty("throughput", "100").toInt
  val userName: String = getProperty("userName", "tst_mat_user1")
  val token: String = "Bearer ".concat(getProperty("token", ""))
  val ags: String = getProperty("ags", "DIVER")
  val sdlc: String = getProperty("sdlc", "DEV")

  /*Environment variables for API POST view */
  val columns: Int = getProperty("columns", "20").toInt
  val records: Int = getProperty("records", "100000").toInt
  val page: String = getProperty("page", "First")
  val sortOrder: String = getProperty("sortOrder", "ASC")

  /* Materialization Scenario command line arguments */
  val materializationScenario: String = getProperty("materializationScenario", "eqtySingleSmall")

  /* First Segment of Materialization Scenario command line arguments */
  val firstSegmentRampUsers: Int = getProperty("firstSegmentRampUsers", "20").toInt
  val firstSegmentUsersPerSecond: Double = getProperty("firstSegmentUsersPerSecond", "1").toDouble
  val firstSegmentUsersStart: Int = getProperty("firstSegmentUsersStart", "5").toInt
  val firstSegmentPeriodStart: Int = getProperty("firstSegmentPeriodStart", "20").toInt
  val firstSegmentUsersEnd: Int = getProperty("firstSegmentUsersEnd", "50").toInt
  val firstSegmentPeriodEnd: Int = getProperty("firstSegmentPeriodEnd", "20").toInt
  val firstSegmentDuration: Double = getProperty("firstSegmentDuration", "300").toDouble
  val firstSegmentPause: Int = getProperty("firstSegmentPause", "60").toInt

  /* Second Segment of Materialization Scenario command line arguments */
  val secondSegmentRampUsers: Int = getProperty("secondSegmentRampUsers", "20").toInt
  val secondSegmentUsersPerSecond: Double = getProperty("secondSegmentUsersPerSecond", "2").toDouble
  val secondSegmentUsersStart: Int = getProperty("secondSegmentUsersStart", "5").toInt
  val secondSegmentPeriodStart: Int = getProperty("secondSegmentPeriodStart", "20").toInt
  val secondSegmentUsersEnd: Int = getProperty("secondSegmentUsersEnd", "50").toInt
  val secondSegmentPeriodEnd: Int = getProperty("secondSegmentPeriodEnd", "20").toInt
  val secondSegmentDuration: Double = getProperty("secondSegmentDuration", "300").toDouble
  val secondSegmentPause: Int = getProperty("secondSegmentPause", "60").toInt

  val dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
  val timeFormat:SimpleDateFormat = new SimpleDateFormat("HH:mm:ss")
  val dateTimeFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
  val headers_API = Map("Accept" -> "application/json", "Content-Type" -> "application/json", "Authorization" -> token)
  val headers_download_API = Map("Accept" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Content-Type" -> "application/json", "Accept-Encoding" -> "gzip,deflate", "Authorization" -> token)

  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl).headers(headers_API)

  println("-------------- ************** ----------------- ")
  println("BaseURL : " + baseUrl)
  println("Users : " + users)
  println("Threshold : " + threshold)
  println("Ramp-Up : " + rampup)
  println("Throughput : " + throughput)
  println("Test User : " + userName)
  println("-------------- ************** ----------------- ")
}
