package blogtech.http

import cats.effect.IO
import org.http4s.HttpService


/**
  *
  */
object Http {
  private val services: Service = Login andThen
                                  Overview andThen
                                  Article andThen
                                  Tool

  val httpService: HttpService[IO] = services.service

}
