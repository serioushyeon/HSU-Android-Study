package com.example.flo_clone.core.domain.repository

import com.example.flo_clone.core.data.model.remote.Result

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}