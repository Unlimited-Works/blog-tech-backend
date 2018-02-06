package blogtech.dao

import doobie.implicits._

/**
  *
  */
object UserDao {
  case class UserData(oid: String, username: String, email: String, password: String)

  def getUserByName(name: String) = {
    val program3 =
      sql"""select oid, username, email, password from users where username=$name"""
        .query[UserData]
        .option

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
