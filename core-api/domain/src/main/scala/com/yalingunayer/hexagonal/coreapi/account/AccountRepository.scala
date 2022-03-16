package com.yalingunayer.hexagonal.coreapi.account

import cats.data.OptionT
import com.yalingunayer.hexagonal.coreapi.account.command.*
import com.yalingunayer.hexagonal.coreapi.account.model.*

trait AccountRepository[F[_]] {
  def existsByEmail(email: String): F[Boolean]
  def existsByGsmNumber(gsmNumber: String): F[Boolean]
  def create(command: RegisterAccountCommand): F[Account]
  def findByActivationTokenNotExpired(activationToken: String): OptionT[F, Account]
  def markAsActivated(account: Account): F[Unit]
  def findByEmailAddressAndPassword(email: String, password: String): OptionT[F, Account]
  def findByEmail(email: String): OptionT[F, Account]
  def retrieve(id: Long): OptionT[F, Account]
}
