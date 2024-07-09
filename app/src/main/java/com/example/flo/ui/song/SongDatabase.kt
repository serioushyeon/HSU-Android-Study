package com.example.flo.ui.song

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.data.local.dao.UserDao
import com.example.flo.data.entitles.Like
import com.example.flo.data.entitles.Song
import com.example.flo.data.entitles.User
import com.example.flo.ui.main.album.Album
import com.example.flo.ui.main.album.AlbumDao

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun userDao() : UserDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : SongDatabase? {
            if(instance == null) {
                synchronized(SongDatabase::class) {
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