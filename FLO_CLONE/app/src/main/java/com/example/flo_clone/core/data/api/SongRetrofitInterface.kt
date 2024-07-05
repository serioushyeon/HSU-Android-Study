package com.example.flo_clone.core.data.api

import com.example.flo_clone.core.data.model.remote.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterface {

    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}