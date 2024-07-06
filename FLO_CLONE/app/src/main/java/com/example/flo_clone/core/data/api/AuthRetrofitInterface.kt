package com.example.flo_clone.core.data.api

import com.example.flo_clone.core.data.model.remote.AuthResponse
import com.example.flo_clone.core.data.model.local.entities.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {

    @POST("/users")
    fun signUp(@Body userEntity: UserEntity): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body userEntity: UserEntity): Call<AuthResponse>
}