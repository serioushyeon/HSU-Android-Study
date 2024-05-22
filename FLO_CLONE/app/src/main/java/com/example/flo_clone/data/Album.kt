package com.example.flo_clone.data

data class Album (
    var title: String? = "",
    var singer: String? = "",
    var converImg: Int? = null,
    var songs: ArrayList<Song>? = null
)