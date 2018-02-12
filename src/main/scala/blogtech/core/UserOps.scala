package blogtech.core

import blogtech.dao.UserDao
import blogtech.util.{Helper, ObjectId}
import cats.effect.IO
/**
  *
  */
class UserOps {
  def verifyUserPassword(account: String, password: String) = {
    UserDao.getUserByNameOrEmail(account)
      .map(x =>
        x.fold(false)(_.password == Helper.toMd5(password))
      )
  }

  def createAccount(userName: String, email: String, password: String) = {
    UserDao.getUserByNameOrEmail(userName, email) flatMap {
      case Some(_) =>
          IO(None)
      case None =>
        UserDao.createUser(UserDao.UserData(new ObjectId().toString, userName, email, password))
    }
  }

}
