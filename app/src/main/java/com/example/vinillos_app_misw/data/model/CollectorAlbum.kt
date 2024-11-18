package com.example.vinillos_app_misw.data.model

import androidx.room.*

//data class CollectorAlbum(
//    val id: Int,
//    val price: Double,
//    val status: String
//)

@Entity(tableName = "collector_album_table")
data class CollectorAlbum(
    @PrimaryKey val id: Int,
    val collectorId: Int, // Foreign Key
    val price: Double,
    val status: String
)
