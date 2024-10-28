package com.example.vinillos_app_misw.data.model

/*
    * Clase de datos que modela el comportamiento de un comentario, los comentarios
    * pertenencen a la clase de datos Album
*/
data class Comment(
    val id: Int,
    val description: String,
    val rating: Int
)