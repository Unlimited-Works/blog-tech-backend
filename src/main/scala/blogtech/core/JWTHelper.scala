package blogtech.core

import java.util.Date

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.json4s.JsonAST.JString
import org.json4s.{DefaultFormats, JObject}
import org.json4s.native.JsonMethods._
import org.json4s.Extraction._
import scala.collection.convert.ImplicitConversions._
import scala.util.Try

/**
  *
  */
object JWTHelper {
  private val algorithm = Algorithm.HMAC256("sec")
  private val verifier = JWT.require(algorithm).build
  private implicit val format: DefaultFormats = org.json4s.DefaultFormats

  //saved keys
  case class PayLoad(userName: String)

//  def contentOf[T](token: String, jValue: JObject => T): Option[T] = {
//    Try {
//      val xx = verifier
//        .verify(token)
//        .getClaims
//      jValue(
//        parse(xx)
//          .extract[JObject])
//    }.recover{
//      case x =>
//        x
//        println(x.getStackTraceString)
//
//        throw x
//    }.toOption
//  }
//
//
//  def contentEntity(token: String): Option[PayLoad] = {
//    Try(
//      parse(verifier.verify(token).getPayload).extract[PayLoad]
//    ).toOption
//  }

  def userName(token: String): Option[String] = {
    Try{verifier
      .verify(token)
      .getClaim("userName").asString()}.toOption
//    contentOf(token, x => {
//      val JString(userName) = x \ "userName"
//      userName
//    })
  }

  def create(data: Map[String, String]): String = {
    val jwt = JWT.create
      .withHeader(Map("typ" -> "JWT"))
      .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365L))

    data.foreach(x => jwt.withClaim(x._1, x._2))

    jwt.sign(algorithm)
  }

  def create[T: Manifest](data: T): String = {
    val map = decompose(data).extract[Map[String, String]]
    create(map)
  }
}
