package com.yalingunayer.hexagonal.coreapi.account

import cats.*
import cats.data.OptionT
import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.hikari.HikariTransactor
import doobie.implicits.*
import doobie.implicits.javatime.*
import doobie.implicits.javasql._
import com.yalingunayer.hexagonal.coreapi.account.command.RegisterAccountCommand
import com.yalingunayer.hexagonal.coreapi.account.model.Account
import com.yalingunayer.hexagonal.coreapi.common.Status
import com.yalingunayer.hexagonal.coreapi.security.PasswordEncoder
import doobie.implicits.legacy.instant._

import java.time.ZonedDateTime
import java.util.UUID

private object SQL {
  import com.yalingunayer.hexagonal.coreapi.common.sql.implicits.*

  def insert(e: AccountEntity): Update0 = sql"""
    INSERT INTO account (created_at, status, first_name, last_name, email, is_master, gsm_number, password, activation_token, activation_token_expires_at)
    VALUES (${e.createdAt.toInstant}, ${e.status}, ${e.firstName}, ${e.lastName}, ${e.email}, ${e.isMaster}, ${e.gsmNumber}, ${e.password}, ${e.activationToken}, ${e.activationTokenExpiresAt.map(_.toInstant)})
  """.update

  def countByEmail(email: String): Query0[Long] = sql"""
    SELECT COUNT(*) FROM account WHERE email = $email AND status <> -1
  """.query[Long]

  def countByGsmNumber(gsmNumber: String): Query0[Long] = sql"""
    SELECT COUNT(*) FROM account WHERE gsm_number = $gsmNumber AND status <> -1
  """.query[Long]

  def findByEmailAddress(email: String): Query0[AccountEntity] = sql"""
    SELECT * FROM account WHERE email = $email AND status <> -1
  """.query[AccountEntity]
}

class AccountDoobieRepository[F[_]: Async](xa: Transactor[F], passwordEncoder: PasswordEncoder[F]) extends AccountRepository[F] {
  override def create(command: RegisterAccountCommand): F[Account] =
    for {
      hashedPassword <- passwordEncoder.encode(command.password)
      entity = AccountEntity(
        0L,
        ZonedDateTime.now(),
        None,
        Status.ACTIVE,
        command.firstName,
        command.lastName,
        command.email,
        true,
        command.gsmNumber,
        hashedPassword,
        UUID.randomUUID().toString,
        Some(ZonedDateTime.now().plusDays(1))
      )
      result <-
        SQL.insert(entity)
          .withUniqueGeneratedKeys[Long]("id")
          .map(id => entity.copy(id = id).model)
          .transact(xa)
    } yield result

  override def existsByEmail(email: String): F[Boolean] = SQL.countByEmail(email).unique.map(_ > 0).transact(xa)

  override def existsByGsmNumber(gsmNumber: String): F[Boolean] = SQL.countByGsmNumber(gsmNumber).unique.map(_ > 0).transact(xa)

  override def findByActivationTokenNotExpired(activationToken: String): OptionT[F, Account] = ???

  override def markAsActivated(account: Account): F[Unit] = ???

  override def findByEmailAddressAndPassword(email: String, password: String): OptionT[F, Account] =
    OptionT(
      SQL.findByEmailAddress(email)
        .option
        .transact(xa)
    ).semiflatTap(entity => passwordEncoder.verify(password, entity.password))
      .map(_.model)

  override def findByEmail(email: String): OptionT[F, Account] = ???

  override def retrieve(id: Long): OptionT[F, Account] = ???
}

object AccountDoobieRepository {
  def apply[F[_]: Async](xa: HikariTransactor[F], passwordEncoder: PasswordEncoder[F]) =
    new AccountDoobieRepository[F](xa, passwordEncoder)
}
