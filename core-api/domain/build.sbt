name := "core-api:domain"

val catsEffectVersion = "3.3.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "com.novocode" % "junit-interface" % "0.11" % "test"
)
