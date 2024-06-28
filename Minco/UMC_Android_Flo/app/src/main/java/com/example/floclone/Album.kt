package com.example.floclone
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.ArrayList

//오늘 발매음악에 들어갈 데이터
@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false)var id: Int = 0,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,

    //var songs: ArrayList<Song>? = null

)
