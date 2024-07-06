package com.example.flo_clone.core.data.model.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flo_clone.core.data.model.local.entities.AlbumEntity
import com.example.flo_clone.core.data.model.local.entities.LikeEntity

@Dao
interface AlbumDao {
    @Insert
    fun insert(albumEntity: AlbumEntity)

    @Update
    fun update(albumEntity: AlbumEntity)

    @Delete
    fun delete(albumEntity: AlbumEntity)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<AlbumEntity>

    @Insert
    fun likeAlbum(like: LikeEntity)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId: Int, albumId: Int): Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikedAlbum(userId: Int, albumId: Int)

    @Query("SELECT A.* FROM LikeTable as LT LEFT JOIN AlbumTable as A ON LT.albumId = A.id WHERE LT.userID = :userId")
    fun getLikedAlbums(userId: Int): List<AlbumEntity>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): AlbumEntity

//    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
//    fun disLikeAlbum(userId: Int, albumId: Int)
// @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")

}