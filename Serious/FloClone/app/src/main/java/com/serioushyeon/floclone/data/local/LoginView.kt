package com.serioushyeon.floclone.data.local

import com.serioushyeon.floclone.data.remote.Result


interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure()
}