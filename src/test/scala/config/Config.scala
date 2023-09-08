package config

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.nio.charset.StandardCharsets
import java.util.Base64

//this file will have common variables
object Config {

  val urlValue = System.getProperty("env")
  val username = "belmont.dis_iris_10@roche.com"
  val password = "a2xlczBsZS5raW4yQkVBUA=="


  val nbUsers = Integer.getInteger("users", 1)
  val myRamp = java.lang.Long.getLong("ramp", 0L)
  val users = System.getProperty("users")
  val ramp = System.getProperty("ramp")

  val httpConf = http
    .baseUrl(urlValue)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .acceptEncodingHeader("gzip, deflate, br")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")

  val AUTHORIZATION = "authorization"
  val bodiesDirectory = System.getProperty("user.dir") + "/src/test/resources/"

}
