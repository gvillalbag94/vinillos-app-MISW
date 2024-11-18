package com.example.vinillos_app_misw.data.model

import androidx.room.*

/*
    * Clase de datos que modela el comportamiento de un album
*/
//data class Album(
//    val id: Int,
//    val name: String,
//    val cover: String,
//    val releaseDate: String,
//    val description: String,
//    val genre: String,
//    val recordLabel: String,
//    val tracks: List<Track>,
//    val performers: List<Performer>,
//    val comments: List<Comment>
//)

@Entity(tableName = "album_table")
data class Album(
    @PrimaryKey val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)


data class AlbumWithDetails(
    @Embedded val album: Album,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val tracks: List<Track>,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val performers: List<Performer>,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val comments: List<Comment>
)