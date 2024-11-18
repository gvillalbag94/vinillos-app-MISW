package com.example.vinillos_app_misw.data.database.dao

import androidx.room.*
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.CollectorWithAlbums

@Dao
interface CollectorDao {
    @Query("SELECT * FROM collector_table")
    suspend fun getCollectors(): List<Collector>

    @Query("SELECT * FROM collector_table WHERE id = :id")
    suspend fun getCollector(id: Int): Collector?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollector(collector: Collector)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectors(collectors: List<Collector>)

    @Delete
    suspend fun deleteCollector(collector: Collector)
}