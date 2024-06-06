package com.example.flo_clone.data

data class AlbumData (
    var title: String? = "",
    var singer: String? = "",
    var converImg: Int? = null,
    var songs: ArrayList<Song>? = null
)