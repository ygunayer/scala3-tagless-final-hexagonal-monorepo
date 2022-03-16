name := "core-api:infra"

val http4sVersion = "0.23.7"
val doobieVersion = "1.0.0-RC2"
val hikariVersion = "5.0.1"
val circeVersion = "0.15.0-M1"
val pureconfigVersion = "0.17.1"
val bcryptVersion = "4.3.0"
val postgresDriverVersion = "42.3.1"
val woofVersion = "0.4.0"
val i18nVersion = "1.0.3"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,

  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,

  "io.circe" %% "circe-generic" % circeVersion,

  "org.postgresql" % "postgresql" % postgresDriverVersion,

  "org.legogroup" % "woof-core_3" % woofVersion,
  "org.legogroup" % "woof-http4s_3" % woofVersion,

  ("com.github.t3hnar" %% "scala-bcrypt" % bcryptVersion).cross(CrossVersion.for3Use2_13),
  ("com.osinka.i18n" % "scala-i18n" % i18nVersion).cross(CrossVersion.for3Use2_13),

  "com.github.pureconfig" %% "pureconfig-core" % pureconfigVersion,

  "com.novocode" % "junit-interface" % "0.11" % "test"
)

