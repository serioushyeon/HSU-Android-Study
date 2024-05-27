package com.example.practice.data

data class Song(
    var singer : String,
    var title : String
)

data class SongEx(
    var title: String="",
    var singer: String="",
    var second: Int = 0,
    var playTime: Int =0,
    var isPlaying : Boolean = false,
    var music : String = ""
)