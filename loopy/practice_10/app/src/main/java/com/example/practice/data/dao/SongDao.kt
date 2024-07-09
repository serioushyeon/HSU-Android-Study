package com.example.practice.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.practice.data.entity.Song

@Dao
interface SongDao {
    @Insert
    fun insert(song : Song)

    @Update
    fun update(song : Song)

    @Delete
    fun delete(song : Song)

    @Query("DELETE FROM songTable")
    fun deleteAllSongs()

    @Query("SELECT * FROM songTable")
    fun getSongs() : List<Song>

    @Query("SELECT * FROM songTable WHERE id = :id")
    fun getSong(id : Int) : Song

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikeSongs(isLike: Boolean) : List<Song>


}