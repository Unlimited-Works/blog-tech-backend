package blogtech.core

import blogtech.util.dao.userDao
import blogtech.util.{Helper, ObjectId}
import cats.effect.IO
/**
  *
  */
class UserOps {
  def verifyUserPassword(account: String, password: String) = {
    userDao.getUserByNameOrEmail(account)
      .map(x =>
        x.fold(false)(_.password == Helper.toMd5(password))
      )
  }

  def createAccount(userName: String, email: String, password: String) = {
    userDao.getUserByNameOrEmail(userName, email) flatMap {
      case Some(_) =>
          IO(None)
      case None =>
        userDao.createUser(userDao.UserData(new ObjectId().toString, userName, email, password))
    }
  }

}
