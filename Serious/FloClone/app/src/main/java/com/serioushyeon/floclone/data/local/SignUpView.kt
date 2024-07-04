package com.serioushyeon.floclone.data.local

import com.serioushyeon.floclone.data.remote.AuthResponse

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(resp: AuthResponse)
}