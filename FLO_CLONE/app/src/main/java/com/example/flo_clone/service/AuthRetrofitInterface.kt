package com.example.flo_clone.service

import com.example.flo_clone.data.AuthResponse
import com.example.flo_clone.room.entity.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {

    @POST("/users")
    fun signUp(@Body userEntity: UserEntity): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body userEntity: UserEntity): Call<AuthResponse>
}