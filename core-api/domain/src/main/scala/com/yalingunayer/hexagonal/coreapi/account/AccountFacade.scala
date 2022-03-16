package com.yalingunayer.hexagonal.coreapi.account

import cats.*
import cats.effect.Async
import com.yalingunayer.hexagonal.coreapi.account.command.RegisterAccountCommand
import com.yalingunayer.hexagonal.coreapi.common.CoreApiBusinessException

class AccountFacade[F[_]](accountRepository: AccountRepository[F])(implicit F: Async[F] with MonadError[F, _]) {
  def register(command: RegisterAccountCommand): F[Unit] =
    F.ifM(accountRepository.existsByEmail(command.email))(
      F.raiseError(CoreApiBusinessException("coreapi.account.emailAlreadyRegistered")),
      F.void(accountRepository.create(command))
    )
}

object AccountFacade {
  def apply[F[_]](accountRepository: AccountRepository[F])(implicit F: Async[F] with MonadError[F, _]): AccountFacade[F] =
    new AccountFacade[F](accountRepository)
}
