package com.example.practice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practice.data.Album
import com.example.practice.data.Song


@Database(entities = [Song::class, Album::class], version = 4,exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao() : AlbumDao
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