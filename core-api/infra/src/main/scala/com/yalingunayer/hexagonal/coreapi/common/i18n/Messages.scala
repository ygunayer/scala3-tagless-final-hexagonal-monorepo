package com.yalingunayer.hexagonal.coreapi.common.i18n

import com.yalingunayer.hexagonal.coreapi.common.CoreApiError

import java.util.{Locale, ResourceBundle}

class Messages(bundle: ResourceBundle) {
  private def interpolate(message: String, args: Any*): String =
    args.zipWithIndex.foldLeft(message) {
      case (str, (arg, idx)) => str.replaceAll("{" + idx + "}", String.valueOf(arg))
    }

  def get(key: String): String =
    Option(bundle.getString(key)).getOrElse(key)

  def get(key: String, args: Any*): String =
    interpolate(get(key), args)

  def getError(key: String): CoreApiError =
    get(key).split(";") match {
      case Array(code, rest*) => CoreApiError(code, rest.mkString(""))
      case _ => CoreApiError("", key)
    }

  def getError(key: String, args: Any*): CoreApiError =
    get(key).split(";") match {
      case Array(code, rest*) => CoreApiError(code, interpolate(rest.mkString(""), args))
      case _ => CoreApiError("", key)
    }
}

object Messages {
  val bundleName = "i18n/messages"

  lazy val en = new Messages(ResourceBundle.getBundle(bundleName, Locale.ENGLISH))

  lazy val bundles: Map[Locale, Messages] = Map(
    Locale.ENGLISH -> en
  )

  def apply(locale: Locale): Messages = bundles.getOrElse(locale, en)
}
