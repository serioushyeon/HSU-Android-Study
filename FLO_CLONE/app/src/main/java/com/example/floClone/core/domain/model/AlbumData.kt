package com.example.floClone.core.domain.model

data class AlbumData (
    var title: String? = "",
    var singer: String? = "",
    var converImg: Int? = null,
    var songs: ArrayList<Song>? = null
)