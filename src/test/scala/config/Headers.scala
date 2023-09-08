package config

import java.nio.charset.StandardCharsets
import java.util.Base64

import config.Config.username
import config.Config.password

object Headers {

  var headers: Map[String, String] = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Authorization" -> "Bearer ${token}"
  )

  val authorization = "Basic " + new String(Base64.getEncoder.encodeToString((username + ":" + new String(Base64.getDecoder.decode(password))).getBytes(StandardCharsets.UTF_8)))

  var authHeader: Map[String, String] = Map(
    "Authorization" -> authorization
  )

}

