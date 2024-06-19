package com.serioushyeon.floclone

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//제목, 가수, 사진,재생시간,현재 재생시간, isplaying(재생 되고 있는지)
@Entity(tableName = "SongTable",
    foreignKeys = [ForeignKey(
        entity = Album::class,
        childColumns = ["albumIdx"],
        parentColumns = ["id"]
    )])
data class Song(
    var title: String = "",
    var singer: String = "",
    var img: Int = 0,
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var isRepeating: Boolean = false,
    var isLike: Boolean = false,
    var albumIdx: Int = 0
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
