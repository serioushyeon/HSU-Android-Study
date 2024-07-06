package com.example.flo_clone.core.data.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class LikeEntity (
    var userID: Int,
    var albumId: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}