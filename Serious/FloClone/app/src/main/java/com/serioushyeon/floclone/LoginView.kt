package com.serioushyeon.floclone


interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure()
}