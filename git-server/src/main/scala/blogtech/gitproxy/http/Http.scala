package blogtech.gitproxy.http

import cats.effect.IO
import org.http4s.HttpService

/**
  *
  */
object Http {
  private val services: Service = Proxy

  val httpService: HttpService[IO] = services.service


}
