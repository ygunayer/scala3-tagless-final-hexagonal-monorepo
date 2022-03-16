package com.yalingunayer.hexagonal.coreapi.common

enum Status(val value: Int):
  case DELETED extends Status(-1)
  case PASSIVE extends Status(0)
  case ACTIVE extends Status(1)

object Status {
  def apply(value: Int): Status = value match {
    case 0 => PASSIVE
    case 1 => ACTIVE
    case -1 => DELETED
    case _ => throw new IllegalArgumentException(f"Invalid Status value $value")
  }
}
