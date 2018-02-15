package blogtech.util

import java.util.Date

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.json4s.Extraction._
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.util.Try
import scala.collection.convert.ImplicitConversions._

/**
  *
  */
object JWTHelper {
  private val algorithm = Algorithm.HMAC256("sec")
  private val verifier = JWT.require(algorithm).build
  private implicit val format: DefaultFormats = org.json4s.DefaultFormats

  //saved keys
  case class PayLoad(userName: String)

  def contentOf[T](token: String, jValue: JObject => T): Option[T] = {
    Try(
      jValue(parse(verifier.verify(token).getPayload).extract[JObject])
    ).toOption
  }


  def contentEntity(token: String): Option[PayLoad] = {
    Try(
      parse(verifier.verify(token).getPayload).extract[PayLoad]
    ).toOption
  }

  def userName(token: String): Option[String] = {
    contentOf(token, x => {
      val JString(userName) = x \ "userName"
      userName
    })
  }

  def create(data: Map[String, String]): String = {
    val jwt = JWT.create
      .withHeader(Map("typ" -> "JWT"))
      .withExpiresAt(new Date(System.currentTimeMillis()))

    data.foreach(x => jwt.withClaim(x._1, x._2))

    jwt.sign(algorithm)
  }

  def create[T: Manifest](data: T): String = {
    val map = decompose(data).extract[Map[String, String]]
    create(map)
  }
}
