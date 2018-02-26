package blogtech

import lorance.scall.{Auth, Config, Password, SessionPool}
import org.slf4j.LoggerFactory

import scala.util.control.NonFatal

/**
  *
  */
package object core {
  private val logger = LoggerFactory.getLogger(getClass)

  val sessionPool =
    new SessionPool(Auth(BlogConfig.sshEnv.host,
      Some(BlogConfig.sshEnv.user),
      22,
      Password(BlogConfig.sshEnv.password)),
      Config(10, 5, 2),
      Runtime.getRuntime.availableProcessors + 2 //support running u=on one core cpu
    )

  val gitOps = doLog(new GitOps(sessionPool), "git ops init")

  val userOps = doLog(new UserOps(), "userOps init")

  def doLog[T](f: => T, msg: String): T = {
    try{
      f
    } catch {
      case NonFatal(e) =>
        logger.error(msg, e)
        throw e
    }
  }
}
