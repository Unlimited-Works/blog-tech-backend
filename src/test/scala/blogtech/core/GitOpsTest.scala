package blogtech.core

import lorance.scall.{Auth, Config, Password, SessionTerminal}
import org.junit.Test

/**
  *
  */
class GitOpsTest {
  val sessionTerminal = new SessionTerminal(Auth("localhost", None, 22, Password("xxx")), Config(10, 5, 2))
  val gitOps = new GitOps(sessionTerminal)

  @Test
  def getFile(): Unit = {
    println(gitOps.getFile("blog-tech", "build.sbt"))
  }
}
