package com.example.vinillos_app_misw.data.model

/*
    * Clase de datos que modela el comportamiento de un Artista o Banda, este clase puede ser
    * contenida en la clase de datos de un Album
*/
data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val albums: List<Album>,
    val performerPrizes: List<PerformerPrize>
)
