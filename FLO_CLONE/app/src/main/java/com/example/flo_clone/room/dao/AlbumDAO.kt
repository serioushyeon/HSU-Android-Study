package com.example.flo_clone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flo_clone.room.entity.AlbumEntity

@Dao
interface AlbumDao {
    @Insert
    fun insert(albumEntity: AlbumEntity)

    @Update
    fun update(albumEntity: AlbumEntity)

    @Delete
    fun delete(albumEntity: AlbumEntity)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): AlbumEntity

//    @Insert
//    fun likeAlbum(like: Like)
//
//    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
//    fun disLikeAlbum(userId: Int, albumId: Int)
//
//    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
//    fun isLikedAlbum(userId: Int, albumId: Int): Int?
//
//    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
//    fun getLikedAlbums(userId: Int): List<Album>
}