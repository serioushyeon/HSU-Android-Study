package com.example.flo_clone.service

import com.example.flo_clone.dto.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterface {

    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}