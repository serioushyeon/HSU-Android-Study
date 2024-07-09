package com.example.floClone.core.data.model.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.floClone.core.data.model.local.entities.SongEntity

@Dao
interface SongDao {
    @Insert
    fun insert(song: SongEntity)

    @Update
    fun update(song: SongEntity)

    @Delete
    fun delete(song: SongEntity)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<SongEntity>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): SongEntity

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id= :id")
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike= :isLike")
    fun getLikedSongs(isLike: Boolean): List<SongEntity>

    // 좋아요 = true 인 모든 노래 좋아요 false로 변경
    @Query("UPDATE SongTable SET isLike = 0 WHERE isLike = 1")
    fun updateIsLikeToFalse()

}