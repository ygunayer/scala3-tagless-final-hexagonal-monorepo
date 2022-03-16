package com.yalingunayer.hexagonal.coreapi

import cats.data.{EitherT, NonEmptyList}
import cats.effect.{Resource, Sync}
import cats.implicits.*
import com.zaxxer.hikari.HikariConfig
import pureconfig.*
import pureconfig.ConfigReader.Result
import pureconfig.error.ConfigReaderException
import pureconfig.generic.derivation.default.*

import java.util.Properties
import scala.reflect.ClassTag

case class HikariConfig(
                         minimumIdle: Int = 5,
                         maximumPoolSize: Int = 100,
                         idleTimeout: Long = 30000,
                         poolName: String = "core-api",
                         maxLifetime: Long = 2000000,
                         connectionTimeout: Long = 30000) derives ConfigReader {
}

case class DatabaseConfig(url: String, driver: String, username: String, password: String, hikari: HikariConfig = HikariConfig()) derives ConfigReader {
  def asHikariConfig(): com.zaxxer.hikari.HikariConfig =
    val cfg = new com.zaxxer.hikari.HikariConfig(new Properties())
    cfg.setJdbcUrl(url)
    cfg.setUsername(username)
    cfg.setPassword(password)
    cfg.setMinimumIdle(hikari.minimumIdle)
    cfg.setIdleTimeout(hikari.idleTimeout)
    cfg.setPoolName(hikari.poolName)
    cfg.setMaxLifetime(hikari.maxLifetime)
    cfg.setConnectionTimeout(hikari.connectionTimeout)
    cfg
}

case class SecurityConfig(passwordEncoderStrength: Int = 8) derives ConfigReader

case class ServerConfig(hostname: String = "localhost", port: Int = 9000) derives ConfigReader

case class CoreApiConfig(server: ServerConfig = ServerConfig(),
                         security: SecurityConfig = SecurityConfig(),
                         db: DatabaseConfig) derives ConfigReader

package object config {
  def load[T, F[_]](source: ConfigSource)(implicit F: Sync[F], reader: ConfigReader[T], classTag: ClassTag[T]): F[T] =
      EitherT(F.blocking(source.cursor()))
        .subflatMap(reader.from)
        .leftMap(ConfigReaderException[T])
        .rethrowT

  def load[T, F[_]](implicit F: Sync[F], reader: ConfigReader[T], classTag: ClassTag[T]): F[T] =
    load(ConfigSource.default)

  object CoreApiConfig {
    def default[F[_]: Sync]: F[CoreApiConfig] = load[CoreApiConfig, F]
  }
}
