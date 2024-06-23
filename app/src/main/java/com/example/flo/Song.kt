package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    val title : String = "",
    val singer : String = "",
    var second : Int = 0,
    var playTime : Int = 60,
    var isPlaying : Boolean = false,
    var isLike : Boolean = false,
    var music : String = "",
    var coverImg : Int? = null
) {
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}
