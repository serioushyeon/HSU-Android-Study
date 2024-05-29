package com.example.practice

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.practice.data.Album

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM albumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>


    @Query("SELECT * FROM albumTable WHERE id = :id")
    fun getAlbum(id: Int): Album


}