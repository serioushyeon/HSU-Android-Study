package com.example.floClone.core.data.api

import com.example.floClone.core.data.model.remote.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterface {

    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}