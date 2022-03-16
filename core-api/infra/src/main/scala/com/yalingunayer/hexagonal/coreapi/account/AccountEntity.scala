package com.yalingunayer.hexagonal.coreapi.account

import com.yalingunayer.hexagonal.coreapi.common.Status
import com.yalingunayer.hexagonal.coreapi.account.model.Account

import java.time.ZonedDateTime

case class AccountEntity(id: Long,
                         createdAt: ZonedDateTime,
                         updatedAt: Option[ZonedDateTime],
                         status: Status,
                         firstName: String,
                         lastName: String,
                         email: String,
                         isMaster: Boolean,
                         gsmNumber: String,
                         password: String,
                         activationToken: String,
                         activationTokenExpiresAt: Option[ZonedDateTime]
                        ) {
  def model: Account = Account(id, firstName, lastName, email, gsmNumber)
}
