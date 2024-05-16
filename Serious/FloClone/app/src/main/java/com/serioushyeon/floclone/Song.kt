package com.serioushyeon.floclone

data class Song(
    val title: String = "",
    val singer:  String = "",
    val img: Int = 0,
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var isRepeating: Boolean = false
)
