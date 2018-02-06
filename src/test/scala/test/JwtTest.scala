package test

import java.util.Date

import org.junit.Test
import scala.collection.convert.ImplicitConversions._

/**
  *
  */
class JwtTest {

  @Test
  def codec = {
    import com.auth0.jwt.JWT
    import com.auth0.jwt.algorithms.Algorithm
    val expireAt = System.currentTimeMillis() + 60 * 1000
    val userId = "userid0"
    val algorithm = Algorithm.HMAC256("sec")

    val x = JWT.create
      .withHeader(Map("typ" -> "JWT"))
      .withExpiresAt(new Date(expireAt))
      .withClaim("userId", userId)
      .sign(algorithm)

    println("x - " + x)
    val verifier = JWT.require(algorithm).build
    val y = verifier.verify(x)

    println("y1 - " + y.getHeader)
    println("y2 - " + y.getPayload)
    println("y3 - " + y.getSignature)
    println("y4 - " + y.getToken)
    println("y5 - " + {
      val xx = y.getClaims.map(x => x._2).mkString(",,,")
      xx
    })

  }


}
