package com.example.flo_clone.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class UserEntity (
    var email: String,
    var password: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}