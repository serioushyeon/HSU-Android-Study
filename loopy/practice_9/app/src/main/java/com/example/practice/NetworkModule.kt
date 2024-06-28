package com.example.practice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
val searchTerm = "Taylor Swift" // 검색할 아티스트나 곡명
const val BASE_URL = "https://itunes.apple.com/"

fun getRetrofit():Retrofit{
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}
class NetworkModule {

}