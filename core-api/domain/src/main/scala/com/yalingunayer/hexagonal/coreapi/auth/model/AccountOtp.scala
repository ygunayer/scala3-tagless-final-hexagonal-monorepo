package com.yalingunayer.hexagonal.coreapi.auth.model

import java.time.{Duration, ZonedDateTime}

case class AccountOtp(accountId: Long,
                      verificationToken: String,
                      otpCode: String,
                      expiresAt: ZonedDateTime) {

  def expiresIn: Long = Duration.between(ZonedDateTime.now(), expiresAt).toMillis
}
