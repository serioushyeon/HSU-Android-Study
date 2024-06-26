package com.example.flo_clone.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo_clone.room.dao.AlbumDao
import com.example.flo_clone.room.dao.SongDao
import com.example.flo_clone.room.entity.AlbumEntity
import com.example.flo_clone.room.entity.SongEntity

@Database(entities = [SongEntity::class, AlbumEntity::class], version = 1)
abstract class SongDatabase: RoomDatabase() {

    // DB 다루기 위한 DAO 객체를 반환하는 함수
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

    // Singleton 패턴
    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,   // 클래스 지정
                        "song-database"       // DB 이름 지정
                    ).allowMainThreadQueries().build()     // 메인 스레드에 쿼리작업 허용 (비효율적임)
                }
            }

            return instance
        }
    }
}