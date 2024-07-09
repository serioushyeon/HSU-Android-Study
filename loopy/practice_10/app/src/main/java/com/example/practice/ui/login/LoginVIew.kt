package com.example.practice.ui.login

import com.example.practice.data.entity.Result

interface LoginVIew {
    fun onLoginSuccess(code : Int, result: Result)
    fun onLoginFailure()
}