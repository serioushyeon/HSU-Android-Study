package com.example.floclone
import java.util.ArrayList

//오늘 발매음악에 들어갈 데이터
data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null
)
