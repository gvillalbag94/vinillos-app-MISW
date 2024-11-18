package com.example.vinillos_app_misw.data.database

import android.content.Context
import androidx.room.*
import com.example.vinillos_app_misw.data.database.dao.AlbumDao
import com.example.vinillos_app_misw.data.database.dao.CollectorDao
import com.example.vinillos_app_misw.data.database.dao.PerformerDao
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.Comment
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.model.PerformerPrize
import com.example.vinillos_app_misw.data.model.Track

@Database(entities = [
    Album::class,
    Collector::class,
    Performer::class,
    Track::class,
    Comment::class,
    PerformerPrize::class],
    version = 1,
    exportSchema = false,
    )
abstract class VinilosRoomDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun collectorDao(): CollectorDao
    abstract fun performerDao(): PerformerDao

    companion object {

        @Volatile
        private var INSTANCE: VinilosRoomDatabase? = null

        fun getDatabase(context: Context): VinilosRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinilosRoomDatabase::class.java,
                    "vinilos_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}