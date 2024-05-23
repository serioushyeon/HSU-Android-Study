package com.example.practice

data class Album (
    var title:String?=null,
    var singer : String?=null,
    var covering:Int? =null,
    var songs: ArrayList<Song> ? = null,
    var muscic: ArrayList<Music> ? = null,
)