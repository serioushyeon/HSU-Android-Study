package com.example.flo_clone.dto

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName(value = "userIdx") var userIdx: Int,
    @SerializedName(value = "jwt") var jwt: String
)