package com.example.floClone.core.domain.repository

import com.example.floClone.core.data.model.remote.Result

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure()
}