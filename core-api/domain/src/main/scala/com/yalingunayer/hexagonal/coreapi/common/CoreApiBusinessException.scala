package com.yalingunayer.hexagonal.coreapi.common

case class CoreApiBusinessException(override val messageKey: String, override val args: Object*) extends CoreApiException(messageKey, args) {

}
