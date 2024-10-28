package com.example.vinillos_app_misw.presentation.view_model.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vinillos_app_misw.data.model.Usuario
import com.example.vinillos_app_misw.data.repositories.UserRepository


class UserViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {

    private val _user = MutableLiveData<Usuario?>()
    val user: LiveData<Usuario?> get() = _user

    fun saveUser(usuario: Usuario) {
        repository.saveUser(usuario)
    }

    fun loadUser() {
        _user.value = repository.getUser()
    }

    fun clearUser() {
        repository.clearUser()
        _user.value = null
    }
}