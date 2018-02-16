package blogtech.gitproxy.http

import cats.effect.IO
import org.http4s.HttpService
import cats.implicits._

/**
  *
  */
trait Service {
  val service: HttpService[IO]

  def andThen(serviceB: Service): Service = new Service {
    val service = Service.this.service <+> serviceB.service
  }
}
