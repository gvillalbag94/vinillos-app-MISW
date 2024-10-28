package com.example.vinillos_app_misw.data.model


/*
    * Clase de opciones que define los tipos de usuarios disponibles a acceder en la
    * aplicacion
    *
    * COLECCIONISTA: Tipo de usuario que cuenta con permisos para crear Albumes y asociar
    * canciones en albumens.
    *
    * USUARIO: Tipo de usuario que no cuenta con permisos para crear Albumes y asociar
    * canciones en albumens.
*/
enum class TipoUsuario {
    COLECCIONISTA,
    USUARIO
}


/*
    * Clase de datos que modela el comportamiento de una Usuario, principalmente diferencia
    * el tipo de usuario que accede a la App
*/
data class Usuario(
    val tipoUsuario: TipoUsuario
) {}
