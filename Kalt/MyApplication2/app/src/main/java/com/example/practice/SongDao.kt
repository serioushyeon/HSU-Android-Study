package com.example.practice

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT *FROM SongTable")
    fun getSongs() : List<Song>

    @Query("SELECT *FROM SongTable WHERE id = :id")
    fun getSong(id:Int) : Song

    @Query("DELETE FROM SongTable")
    fun deleteAllSongs()

    @Query("UPDATE SongTable SET islike = :islike WHERE id = :id")
    fun updateislikeByid(islike: Boolean,id:Int)

    @Query("SELECT *FROM SongTable WHERE islike = :islike")
    fun getLikedSongs(islike: Boolean):List<Song>
}