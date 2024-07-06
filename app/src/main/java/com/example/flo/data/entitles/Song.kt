package com.example.flo.data.entitles

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.flo.ui.main.album.Album

@Entity(tableName = "SongTable",
    foreignKeys = [ForeignKey(
        entity = Album::class,
        childColumns = ["albumIdx"],
        parentColumns = ["id"]
    )])
data class Song(
    val title : String = "",
    val singer : String = "",
    var second : Int = 0,
    var playTime : Int = 60,
    var isPlaying : Boolean = false,
    var isLike : Boolean = false,
    var music : String = "",
    var coverImg : Int? = null,
    var albumIdx: Int = 0
) {
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}
