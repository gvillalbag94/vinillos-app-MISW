package com.example.vinillos_app_misw.data.model

import androidx.room.*

/*
    * Clase de datos que modela el comportamiento de una Cancion, este clase puede ser
    * contenida en la clase de datos de un Album
*/
//data class Track(
//    val id: Int,
//    val name: String,
//    val duration: String
//)

@Entity(
    tableName = "track_table",
    indices = [Index(value = ["albumId"])],
    foreignKeys = [ForeignKey(
        entity = Album::class,
        parentColumns = ["id"],
        childColumns = ["albumId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Track(
    @PrimaryKey val id: Int,
    val name: String,
    val duration: String,
    val albumId: Int
)