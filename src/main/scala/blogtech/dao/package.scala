package blogtech

import blogtech.core.BlogConfig
import cats.effect.IO
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux

/**
  *
  */
package object dao {
  val driver: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:postgres",
    BlogConfig.dbEnv.user,
    BlogConfig.dbEnv.password
  )
}
