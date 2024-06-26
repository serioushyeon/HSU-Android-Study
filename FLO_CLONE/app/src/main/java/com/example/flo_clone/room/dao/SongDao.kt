package com.example.flo_clone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flo_clone.room.entity.SongEntity

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

}