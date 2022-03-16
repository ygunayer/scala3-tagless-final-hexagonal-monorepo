package com.yalingunayer.hexagonal.coreapi.common

trait CoreApiException(val messageKey: String, val args: Any*) extends RuntimeException {
}
