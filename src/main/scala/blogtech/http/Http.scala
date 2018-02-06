package blogtech.http

import cats.effect.IO
import org.http4s.HttpService


/**
  *
  */
object Http {
  private val services: Service = Login andThen
                                  Overview andThen
                                  Article

  val httpService: HttpService[IO] = services.service

  val jwtCookie = "blogTechJwt"
}
