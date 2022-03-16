package com.yalingunayer.hexagonal.coreapi

import cats.effect.Async
import doobie.hikari.HikariTransactor
import com.yalingunayer.hexagonal.coreapi.account.{AccountDoobieRepository, AccountFacade, AccountRepository}
import com.yalingunayer.hexagonal.coreapi.auth.{AuthController, AuthFacade}
import com.yalingunayer.hexagonal.coreapi.common.rest.{CoreApiHttpErrorHandler, HttpErrorHandler}
import com.yalingunayer.hexagonal.coreapi.security.PasswordEncoder
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.middleware.ErrorAction
import org.legogroup.woof.{Logger, given}

object CoreApiEndpoints {
  def apply[F[_] : Async](xa: HikariTransactor[F], passwordEncoder: PasswordEncoder[F])(using Logger[F]): HttpApp[F] =
    val accountRepository: AccountRepository[F] = AccountDoobieRepository[F](xa, passwordEncoder)

    val authFacade: AuthFacade[F] = AuthFacade(accountRepository)
    val accountFacade: AccountFacade[F] = AccountFacade(accountRepository)

    val app = Router(
      "/api/v1/auth" -> AuthController[F](authFacade, accountFacade).routes
    ).orNotFound

    ErrorAction.httpApp.log(app,
      (t, s) => Logger[F].warn(f"FAILED TO MESSAGE STUFF $t $s"),
      (t, s) => Logger[F].warn(f"SERVICE FAIL $t $s"))
}
