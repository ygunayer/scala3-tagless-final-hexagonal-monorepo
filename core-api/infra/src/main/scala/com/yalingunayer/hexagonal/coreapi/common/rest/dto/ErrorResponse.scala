package com.yalingunayer.hexagonal.coreapi.common.rest.dto

import com.yalingunayer.hexagonal.coreapi.common.{CoreApiError, CoreApiException}
import com.yalingunayer.hexagonal.coreapi.common.i18n.Messages

case class ErrorResponse(error: CoreApiError)

object ErrorResponse {
  def apply(exception: CoreApiException)(implicit messages: Messages): ErrorResponse =
    ErrorResponse(messages.getError(exception.messageKey, exception.args))

  def apply(throwable: Throwable)(implicit messages: Messages): ErrorResponse =
    ErrorResponse(messages.getError("common.error.unspecified"))
}
