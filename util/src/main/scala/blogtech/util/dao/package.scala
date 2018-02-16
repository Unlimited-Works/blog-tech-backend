package blogtech.util

import cats.effect.IO
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux

/**
  *
  */
package object dao {
  val driver: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    s"jdbc:postgresql://${UtilConfig.dbEnv.host}:${UtilConfig.dbEnv.port}/${UtilConfig.dbEnv.database}",
    UtilConfig.dbEnv.user,
    UtilConfig.dbEnv.password
  )

  val userDao = new UserDao(driver)

}
