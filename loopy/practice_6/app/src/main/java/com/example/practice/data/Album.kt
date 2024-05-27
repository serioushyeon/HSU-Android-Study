package com.example.practice.data

data class Album(
    var title: String?="",
    var singer: String?="",
    var coverImg : Int?= null,
    var songs : ArrayList<SongEx>? = null,
    var state : Boolean? = false
)
