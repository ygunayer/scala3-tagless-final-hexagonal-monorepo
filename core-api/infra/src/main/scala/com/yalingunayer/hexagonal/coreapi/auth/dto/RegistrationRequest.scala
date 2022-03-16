package com.yalingunayer.hexagonal.coreapi.auth.dto

import com.yalingunayer.hexagonal.coreapi.account.command.RegisterAccountCommand

case class RegistrationRequest(firstName: String,
                               lastName: String,
                               organizationName: String,
                               email: String,
                               gsmNumber: String,
                               password: String,
                               passwordRepeat: String) {
  def command: RegisterAccountCommand =
    RegisterAccountCommand(firstName, lastName, email, gsmNumber, password)
}
