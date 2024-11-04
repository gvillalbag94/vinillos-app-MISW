package com.example.vinillos_app_misw.data.repositories

import com.example.vinillos_app_misw.data.adapters.UserAdapter
import com.example.vinillos_app_misw.data.model.Usuario

class UserRepository(private val userAdapter: UserAdapter) {

    fun saveUser(usuario: Usuario) {
        userAdapter.saveUser(usuario)
    }

    fun getUser(): Usuario? {
        return userAdapter.getUser()
    }

    fun clearUser() {
        return userAdapter.clearUser()
    }

}