package com.example.practice

interface LoginVIew {
    fun onLoginSuccess(code : Int, result: Result)
    fun onLoginFailure()
}