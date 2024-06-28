package com.serioushyeon.floclone

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(resp: AuthResponse)
}