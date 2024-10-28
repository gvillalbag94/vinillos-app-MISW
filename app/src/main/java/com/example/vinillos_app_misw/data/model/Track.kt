package com.example.vinillos_app_misw.data.model

/*
    * Clase de datos que modela el comportamiento de una Cancion, este clase puede ser
    * contenida en la clase de datos de un Album
*/
data class Track(
    val id: Int,
    val name: String,
    val duration: String
)