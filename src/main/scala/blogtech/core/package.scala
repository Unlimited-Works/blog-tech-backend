package blogtech

import lorance.scall.{Auth, Config, Password, SessionPool}

/**
  *
  */
package object core {
  val sessionPool = new SessionPool(Auth("localhost", None, 22, Password("xxx")), Config(10, 5, 2))
}
