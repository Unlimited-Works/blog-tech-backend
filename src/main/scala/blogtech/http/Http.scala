package blogtech.http


/**
  *
  */
object Http {
  private val services: Service = Login andThen
                                  Login2

  val httpService = services.service
}
