package com.yalingunayer.hexagonal.coreapi.account.model

case class Account(id: Long,
                   firstName: String,
                   lastName: String,
                   email: String,
                   gsmNumber: String) {

  def fullName: String = Seq(firstName, lastName).filter(!_.isBlank).mkString(" ")
}

