package com.yalingunayer.hexagonal.coreapi.auth.command

case class InitiateAuthenticationCommand(email: String,
                                         password: String,
                                         ipAddress: String)
