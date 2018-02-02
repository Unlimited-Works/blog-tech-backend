package blogtech.http


/**
  *
  */
object Http {
  private val services: Service = Login andThen
                                  Overview

  val httpService = services.service
}
