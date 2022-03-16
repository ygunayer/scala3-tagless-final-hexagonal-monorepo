package com.yalingunayer.hexagonal.coreapi.auth.dto

import com.yalingunayer.hexagonal.coreapi.auth.command.InitiateAuthenticationCommand

case class LoginRequest(email: String, password: String) {
  def command: InitiateAuthenticationCommand =
    InitiateAuthenticationCommand(email, password, "1.1.1.1")
}
