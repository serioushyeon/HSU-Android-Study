package com.example.practice

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


data class Album (
    var title:String?=null,
    var singer : String?=null,
    var coverimg:Int? =null,
    var songs: ArrayList<Song> ? = null,
    var muscic: ArrayList<Music> ? = null,
)