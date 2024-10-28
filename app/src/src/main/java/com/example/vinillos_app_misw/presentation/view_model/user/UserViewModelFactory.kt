package com.example.vinillos_app_misw.presentation.view_model.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinillos_app_misw.data.repositories.UserRepository

class UserViewModelFactory(
    private val application: Application,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application,userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}