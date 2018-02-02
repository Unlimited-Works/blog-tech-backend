package blogtech.core

import lorance.scall.{Auth, Config, Password, SessionPool}
import org.junit.Test

/**
  *
  */
class GitOpsTest {
  val sysSession = new SessionPool(Auth(BlogConfig.sshEnv.user,
    None,
    22,
    Password(BlogConfig.sshEnv.password)),
    Config(10, 5, 2)
  )

  val gitOps = new GitOps(sysSession)

  @Test
  def getFile(): Unit = {
    println(gitOps.getFile("blog-tech", "build.sbt"))
  }
}
