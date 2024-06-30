package com.example.flo_clone.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class AlbumEntity(
    @PrimaryKey (autoGenerate = false) var id: Int = 0,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null
)
