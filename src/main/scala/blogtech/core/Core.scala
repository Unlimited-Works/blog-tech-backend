package blogtech.core

import lorance.scall.{Auth, Config, Password, SessionPool}

/**
  *
  */
object Core {
  val sessionPool = new SessionPool(Auth(BlogConfig.sshEnv.host,
    Some(BlogConfig.sshEnv.user),
    22,
    Password(BlogConfig.sshEnv.password)),
    Config(10, 5, 2),
  )

  val gitOps = new GitOps(sessionPool)

  val userOps = new UserOps()
}