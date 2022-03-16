package com.yalingunayer.hexagonal.coreapi.auth

import cats.MonadError
import cats.effect.Async
import com.yalingunayer.hexagonal.coreapi.account.AccountRepository
import com.yalingunayer.hexagonal.coreapi.auth.command.InitiateAuthenticationCommand
import com.yalingunayer.hexagonal.coreapi.auth.model.AccountOtp
import com.yalingunayer.hexagonal.coreapi.common.CoreApiDataNotFoundException

import java.time.ZonedDateTime

class AuthFacade[F[_]](accountRepository: AccountRepository[F])(implicit F: Async[F] with MonadError[F, _]) {
  def initiate(command: InitiateAuthenticationCommand): F[AccountOtp] =
    accountRepository.findByEmailAddressAndPassword(command.email, command.password)
      .map(account => AccountOtp(account.id, "FOO", "BAR", ZonedDateTime.now().plusSeconds(180)))
      .getOrElseF(F.raiseError(new CoreApiDataNotFoundException("no such user")))
}

object AuthFacade {
  def apply[F[_]](accountRepository: AccountRepository[F])(implicit F: Async[F] with MonadError[F, _]): AuthFacade[F] =
    new AuthFacade[F](accountRepository)
}
