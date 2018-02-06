package blogtech.dao

import org.junit.Test

/**
  *
  */
class UserDaoTest {

  @Test
  def findUser(): Unit = {
    println(UserDao.isExistByName("lorance").unsafeRunSync())
    println(UserDao.getUserByName("lorance").unsafeRunSync())
  }
}
