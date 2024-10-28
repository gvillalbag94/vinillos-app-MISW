package com.example.vinillos_app_misw.data.model

/*
    * Clase de datos que modela el comportamiento de un album
*/
data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track>,
    val performers: List<Performer>,
    val comments: List<Comment>
)
