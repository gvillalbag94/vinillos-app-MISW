package com.example.vinillos_app_misw.data.model

import androidx.room.*

/*
    * Clase de datos que modela el comportamiento de un comentario, los comentarios
    * pertenencen a la clase de datos Album
*/
//data class Comment(
//    val id: Int,
//    val description: String,
//    val rating: Int
//)


@Entity(
    tableName = "comment_table",
    indices = [Index(value = ["albumId"]), Index(value = ["collectorId"])],
    foreignKeys = [ForeignKey(
        entity = Album::class,
        parentColumns = ["id"],
        childColumns = ["albumId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Collector::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class Comment(
    @PrimaryKey val id: Int,
    val description: String,
    val rating: Int,
    val albumId: Int?,
    val collectorId: Int?,
)