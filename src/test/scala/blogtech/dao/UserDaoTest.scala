package blogtech.dao

import org.junit.Test
import blogtech.util.dao.userDao

/**
  *
  */
class UserDaoTest {

  @Test
  def findUser(): Unit = {
    println(userDao.isExistByName("lorance").unsafeRunSync())
    println(userDao.getUserByName("lorance").unsafeRunSync())
  }
}
