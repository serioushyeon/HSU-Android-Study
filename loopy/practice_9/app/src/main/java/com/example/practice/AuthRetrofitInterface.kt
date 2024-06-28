package com.example.practice

import com.example.practice.data.User2
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User2): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User2): Call<AuthResponse>




}