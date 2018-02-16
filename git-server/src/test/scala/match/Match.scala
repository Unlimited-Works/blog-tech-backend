package `match`

import org.junit.Test

/**
  *
  */
class Match {
  class IntVar(val int: Int)
  case class DoubleData(a: String, b: Long)
  case class SingleData(a: String)
  object A {
    def unapply(arg: String): Option[IntVar] = {
      if(arg.contains("str")) Some(new IntVar(1))
      else None
    }
  }


  @Test
  def test(): Unit = {
    DoubleData("str", 1) match {
      case x @ (a DoubleData b) =>
        println(s"match: $a $b")
      case a =>
        println("not match: " + a)
    }


    SingleData("str") match {
      case SingleData(a) =>
        println(s"match: $a")
      case a =>
        println("not match: " + a)
    }
  }

  @Test
  def testObject(): Unit = {
    "213" match {
      case xx @ A(a) => ???
    }
  }

  case class :?(a: String, b: Int)
  case class ?(a: String, b: Int)
  case class MatchA(a: String, b: Int)
  object MatchInt {// can't
    def unapply(arg: Int): Option[(Long, Double)] = {
      Some(arg, arg)
    }
  }
  object +& {// or :? or ?: or +& or +
    def unapply(arg: Int): Option[(Long, Double)] = {
      Some(arg, arg)
    }
  }

  object & {// or :? or ?: or +& or +
    def unapply(arg: Int): Option[(Long, Double)] = {
      Some(arg, arg)
    }
  }

  @Test
  def matchOK1(): Unit = {
    MatchA("abc", 123) match {
      case a MatchA (b MatchInt c) =>
        println(s"$a, $b, $c")
    }

  }

  @Test
  def matchOK2(): Unit = {
    MatchA("abc", 123) match {
      case a MatchA b & c =>
        println(s"$a, $b, $c")
    }
  }


//  @Test
//  def matchFail1(): Unit = {
//    MatchA("abc", 123) match {
//      case a MatchA b MatchInt c =>
//        println(s"$a, $b, $c")
//    }
//
//  }

  @Test
  def matchFail2(): Unit = {
    :?("abc", 123) match {
      case a :? b +& c =>
        println(s"$a, $b, $c")
    }

  }

  def last[A](seq: Seq[A]) : A = seq match {
    case head +: Nil => head
    case head +: tail => last(tail)
  }
}


