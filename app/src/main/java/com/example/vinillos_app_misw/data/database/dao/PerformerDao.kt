package com.example.vinillos_app_misw.data.database.dao

import androidx.room.*
import com.example.vinillos_app_misw.data.model.Performer

@Dao
interface PerformerDao {

    // Fetch a single performer by ID
    @Query("SELECT * FROM performer_table WHERE id = :performerId")
    suspend fun getPerformerById(performerId: Int): Performer

    // Fetch all performers
    @Query("SELECT * FROM performer_table")
    suspend fun getAllPerformers(): List<Performer>
}