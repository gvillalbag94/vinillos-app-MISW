package com.example.vinillos_app_misw.data.database.dao

import androidx.room.*
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.CollectorAlbum
import com.example.vinillos_app_misw.data.model.CollectorWithDetails
import com.example.vinillos_app_misw.data.model.Comment
import com.example.vinillos_app_misw.data.model.Performer

@Dao
interface CollectorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollector(collector: Collector)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformer(performer: Performer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorAlbum(collectorAlbum: CollectorAlbum)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)


    @Query("SELECT * FROM collector_table")
    suspend fun getCollectors(): List<CollectorWithDetails>

    @Query("SELECT * FROM collector_table WHERE id = :id")
    suspend fun getCollector(id: Int): CollectorWithDetails?

    @Transaction
    suspend fun insertCollectorWithDetails(collectorWithDetails: CollectorWithDetails) {
        insertCollector(collectorWithDetails.collector)

        collectorWithDetails.collectorAlbums.forEach {
            insertCollectorAlbum(it)
        }

        collectorWithDetails.favoritePerformers .forEach {
            insertPerformer(it)
        }

        collectorWithDetails.comments.forEach {
            insertComment(it)
        }
    }

    @Transaction
    suspend fun insertCollectorsWithDetails(collectorsWithDetails: List<CollectorWithDetails>) {
        for (collectorWithDetails in collectorsWithDetails) {
            insertCollectorWithDetails(collectorWithDetails)
        }
    }
}