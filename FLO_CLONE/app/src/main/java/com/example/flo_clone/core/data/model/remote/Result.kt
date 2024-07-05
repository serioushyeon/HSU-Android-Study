package com.example.flo_clone.core.data.model.remote

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName(value = "userIdx") var userIdx: Int,
    @SerializedName(value = "jwt") var jwt: String
)