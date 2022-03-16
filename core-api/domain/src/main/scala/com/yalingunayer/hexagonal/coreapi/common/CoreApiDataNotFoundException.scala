package com.yalingunayer.hexagonal.coreapi.common

case class CoreApiDataNotFoundException(override val messageKey: String, override val args: Object*) extends CoreApiException(messageKey, args) {

}
