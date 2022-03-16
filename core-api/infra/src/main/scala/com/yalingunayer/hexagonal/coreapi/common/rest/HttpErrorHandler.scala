package com.yalingunayer.hexagonal.coreapi.common.rest

import cats.data.{Kleisli, OptionT}
import cats.syntax.*
import cats.syntax.all.*
import cats.{ApplicativeError, MonadError}
import io.circe.generic.auto.*
import io.circe.syntax.*
import com.yalingunayer.hexagonal.coreapi.common.i18n.Messages
import com.yalingunayer.hexagonal.coreapi.common.rest.dto.ErrorResponse
import com.yalingunayer.hexagonal.coreapi.common.{CoreApiDataNotFoundException, CoreApiException}
import org.http4s.Status.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes, Request, Response}

import java.util.Locale

trait HttpErrorHandler[F[_], E <: Throwable] {
  def handle(routes: HttpRoutes[F]): HttpRoutes[F]
}

class CoreApiHttpErrorHandler[F[_], E <: Throwable](implicit M: MonadError[F, E]) extends HttpErrorHandler[F, E] with Http4sDsl[F] {
  implicit val errorResponseEncoder: EntityEncoder[F, ErrorResponse] = jsonEncoderOf[F, ErrorResponse]

  private val handler: (E, Locale) => F[Response[F]] = {
    case (err, locale) =>
      implicit val messages: Messages = Messages(locale)
      err match {
        case e: CoreApiDataNotFoundException => NotFound(ErrorResponse(e))
        case e: CoreApiException => UnprocessableEntity(ErrorResponse(e))
        case t: Throwable => InternalServerError(ErrorResponse(t))
      }
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    HttpErrorHandler(routes)(handler)
}

object HttpErrorHandler {
  def apply[F[_], E <: Throwable](routes: HttpRoutes[F])(handler: (E, Locale) => F[Response[F]])(implicit ev: ApplicativeError[F, E]): HttpRoutes[F] =
    Kleisli(req =>
      OptionT {
        routes.run(req).value.handleErrorWith { e => handler(e, Locale.ENGLISH).map(Option(_)) }
      }
    )
}
