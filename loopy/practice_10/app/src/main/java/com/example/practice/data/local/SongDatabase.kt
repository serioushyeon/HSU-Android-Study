package com.example.practice.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practice.data.dao.SongDao
import com.example.practice.data.entity.Album
import com.example.practice.data.entity.Like
import com.example.practice.data.entity.Song
import com.example.practice.data.entity.User
import com.example.practice.data.dao.AlbumDao
import com.example.practice.data.dao.UserDao

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 4,exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao() : AlbumDao
    abstract fun userDao() : UserDao
    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : SongDatabase?{
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }

}