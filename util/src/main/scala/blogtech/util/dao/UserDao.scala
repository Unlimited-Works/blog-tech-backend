package blogtech.util.dao

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor.Aux
import org.slf4j.LoggerFactory

/**
  *
  */
class UserDao(driver: Aux[IO, Unit]) {
  private val logger = LoggerFactory.getLogger(getClass)
  case class UserData(oid: String, username: String, email: String, password: String)

  def getUserByName(name: String) = {
    val program3 =
      sql"""select oid, username, email, password from users where username=$name"""
        .query[UserData]
        .option

    program3.transact(driver)
  }

  def getUserByNameOrEmail(nameOrEmail: String) = {
    val program3 =
      sql"""select oid, username, email, password from users where username=$nameOrEmail or email=$nameOrEmail"""
        .query[UserData]
        .option

    program3.transact(driver)
  }

  def getUserByNameOrEmail(name: String, email: String) = {
    val program3 =
      sql"""select oid, username, email, password from users where username=$name or email=$email"""
        .query[UserData]
        .option

    program3.transact(driver)
  }

  def createUser(userData: UserData) = {
    val program3 =
      sql"""insert into users (oid, username, email, password) VALUES(${userData.oid}, ${userData.username}, ${userData.email}, ${userData.password})"""
        .update.run
        .attemptSql
        .map{
          case Left(e) =>
            logger.info(s"CreateUserFail - $userData")
            None
          case Right(count) => Some(userData.oid)
        }

    program3.transact(driver)
  }

  def isExistByName(name: String) = {
    val program3 =
      sql"""select oid from users where username=$name"""
        .query[String]
        .option

    program3.transact(driver)
  }

}

