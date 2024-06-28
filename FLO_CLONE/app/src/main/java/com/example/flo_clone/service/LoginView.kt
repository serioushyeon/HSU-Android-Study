package com.example.flo_clone.service

import com.example.flo_clone.data.Result

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}