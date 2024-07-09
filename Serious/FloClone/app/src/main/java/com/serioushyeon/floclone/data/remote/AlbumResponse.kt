package com.serioushyeon.floclone.data.remote

import com.google.gson.annotations.SerializedName

class AlbumResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AlbumChartResult
)

data class AlbumChartResult(
    @SerializedName("albums") val albums: List<AlbumChartSongs>
)

data class AlbumChartSongs(
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("title") val title:String,
    @SerializedName("singer") val singer: String,
    @SerializedName("coverImgUrl") val coverImgUrl : String
)