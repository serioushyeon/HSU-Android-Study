package com.example.practice

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongRetrofitInterfaces {
    @GET("search?entity=musicTrack")
    fun getSongs(@Query("term") searchTerm: String): Call<iTunesResponse>
}