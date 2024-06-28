package com.example.floclone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 2)
abstract class SongDatabase: RoomDatabase(){
    abstract fun songDao(): SongDao
    abstract fun UserDao(): UserDao
    abstract fun albumDao(): AlbumDao


    companion object{
        private var instance: SongDatabase? = null

//        // Define the migration from version 1 to 2
//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // 예시: 새로운 컬럼 추가
//                database.execSQL("ALTER TABLE Song ADD COLUMN new_column INTEGER")
//            }
//        }

        @Synchronized
        fun getInstance(context: Context): SongDatabase?{
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" //다른 이름이랑 겹치면 꼬임
                    ).allowMainThreadQueries().build() //.addMigrations(MIGRATION_1_2)
                }
            }
            return instance
        }
    }
}