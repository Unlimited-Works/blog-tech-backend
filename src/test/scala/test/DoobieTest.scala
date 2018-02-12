package test

import doobie._
import doobie.implicits._
import org.junit.Test
import cats.data._
import cats.effect._
import cats.implicits._

/**
  *
  */
class DoobieTest {
  val xa = blogtech.dao.driver

  @Test
  def simpleTest: Unit = {
    case class Person(name: String, age: Int)
    val nel = NonEmptyList.of(Person("Bob", 12), Person("Alice", 14))

    println(nel.head)
  }

  @Test
  def connectToDB: Unit = {
    val program1: ConnectionIO[Int] = 42.pure[ConnectionIO]
    println(program1)

    val task: IO[Int] = program1.transact(xa)

    println(task.unsafeRunSync)
  }

  @Test
  def simpleSelectSql: Unit = {

    val program2: ConnectionIO[Int] = sql"select 42".query[Int].unique
    val task2: IO[Int] = program2.transact(xa)
    println(task2.unsafeRunSync)
  }


  @Test
  def forStatment: Unit = {
    val program3: ConnectionIO[(Int, Double)] =
      for {
        a <- fr"select 42".query[Int].unique
        b <- sql"select random()".query[Double].unique
      } yield (a, b)

    println(program3.transact(xa).unsafeRunSync)
  }
}
