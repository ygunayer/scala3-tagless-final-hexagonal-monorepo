name := "core-api"

lazy val domain = project in file("./domain")

lazy val infra = (project in file("./infra"))
  .dependsOn(domain)
