package com.yalingunayer.hexagonal.coreapi.account.command

case class RegisterAccountCommand(firstName: String,
                                  lastName: String,
                                  email: String,
                                  gsmNumber: String,
                                  password: String)
