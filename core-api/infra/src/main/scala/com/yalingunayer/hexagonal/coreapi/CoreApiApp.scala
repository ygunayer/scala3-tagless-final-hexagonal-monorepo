package com.yalingunayer.hexagonal.coreapi

import cats.effect.*
import cats.syntax._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import com.yalingunayer.hexagonal.coreapi.config.CoreApiConfig
import com.yalingunayer.hexagonal.coreapi.security.BCryptPasswordEncoder
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import org.legogroup.woof.{given, *}

object CoreApiApp extends IOApp {
  def createDbTransactor[F[_] : Async](config: CoreApiConfig): Resource[F, HikariTransactor[F]] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[F](config.db.hikari.maximumPoolSize)
      xa <- HikariTransactor.fromHikariConfig(config.db.asHikariConfig(), ec)
    } yield xa

  def createServer[F[_] : Async](config: CoreApiConfig)(using Logger[F]): Resource[F, Server] =
    createDbTransactor[F](config).flatMap {
      xa =>
        val passwordEncoder = BCryptPasswordEncoder[F](config.security.passwordEncoderStrength)
        BlazeServerBuilder[F]
          .bindHttp(config.server.port, config.server.hostname)
          .withHttpApp(CoreApiEndpoints(xa, passwordEncoder))
          .resource
    }

  override def run(args: List[String]): IO[ExitCode] =
    val consoleOutput = new Output[IO]:
      override def output(str: String): IO[Unit] = IO.delay(println(str))
      override def outputError(str: String): IO[Unit] = output(str)

    given Filter = Filter.everything
    given Printer = ColorPrinter()

    for {
      given Logger[IO] <- DefaultLogger.makeIo(consoleOutput)
      config <- CoreApiConfig.default[IO]
      server <- createServer[IO](config)
        .evalTap(server => IO.println(f"Server is now listening at ${server.addressIp4s}"))
        .use(_ => IO.never)
        .as(ExitCode.Success)
    } yield server
}
