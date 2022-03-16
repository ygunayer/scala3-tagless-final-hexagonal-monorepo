package com.yalingunayer.hexagonal.coreapi.common.sql

import doobie.{Get, Put}
import com.yalingunayer.hexagonal.coreapi.common.Status

object implicits {
  implicit val statusGet: Get[Status] = Get[Int].map(Status.apply)
  implicit val statusPut: Put[Status] = Put[Int].contramap(_.value)
}
