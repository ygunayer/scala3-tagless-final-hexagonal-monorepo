package com.yalingunayer.hexagonal.coreapi.security

import cats.Functor
import cats.data.EitherT
import cats.effect.Sync
import com.github.t3hnar.bcrypt.*

trait PasswordEncoder[F[_]: Functor] {
  def encode(input: String): F[String]
  def verify(plain: String, encrypted: String): F[Boolean]
}

class BCryptPasswordEncoder[F[_]](private val strength: Int)(implicit F: Sync[F]) extends PasswordEncoder[F] {
  override def encode(input: String): F[String] =
    EitherT.fromEither(input.bcryptSafeBounded(strength).toEither).rethrowT

  override def verify(plain: String, encrypted: String): F[Boolean] =
    EitherT.fromEither(plain.isBcryptedSafeBounded(encrypted).toEither).rethrowT
}

object BCryptPasswordEncoder {
  def apply[F[_]: Sync](strength: Int): PasswordEncoder[F] =
    new BCryptPasswordEncoder[F](strength)

  def apply[F[_]: Sync](): PasswordEncoder[F] =
    new BCryptPasswordEncoder[F](8)
}
