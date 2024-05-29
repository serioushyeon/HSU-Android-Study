package com.example.practice.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id : Int = 0,
    var title: String?="",
    var singer: String?="",
    var coverImg : Int?= null,
    var state : Boolean? = false
)
