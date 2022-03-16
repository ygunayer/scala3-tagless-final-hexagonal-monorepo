package com.yalingunayer.hexagonal.coreapi.auth

import cats.FlatMap
import cats.implicits.*
import cats.effect.*
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import com.yalingunayer.hexagonal.coreapi.auth.AuthFacade
import com.yalingunayer.hexagonal.coreapi.account.AccountFacade
import com.yalingunayer.hexagonal.coreapi.auth.dto.*

class AuthController[F[_]](authFacade: AuthFacade[F], accountFacade: AccountFacade[F])(implicit F: Async[F] with FlatMap[F]) extends Http4sDsl[F]  {
  implicit val loginRequestDecoder: EntityDecoder[F, LoginRequest] = jsonOf[F, LoginRequest]
  implicit val registrationRequestDecoder: EntityDecoder[F, RegistrationRequest] = jsonOf[F, RegistrationRequest]

  def routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "register" =>
        for {
          request <- req.decodeJson[RegistrationRequest]
          otp <- accountFacade.register(request.command)
          resp <- Ok(otp.asJson)
        } yield resp

      case req @ POST -> Root / "login" =>
        for {
          request <- req.decodeJson[LoginRequest]
          otp <- authFacade.initiate(request.command)
          resp <- Ok(otp.asJson)
        } yield resp
    }
}

object AuthController {
  def apply[F[_]: Async](authFacade: AuthFacade[F], accountFacade: AccountFacade[F]): AuthController[F] =
    new AuthController[F](authFacade, accountFacade)
}
