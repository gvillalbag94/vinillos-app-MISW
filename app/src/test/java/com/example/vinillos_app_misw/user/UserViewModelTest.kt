package com.example.vinillos_app_misw.user

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vinillos_app_misw.data.model.TipoUsuario
import com.example.vinillos_app_misw.data.model.Usuario
import com.example.vinillos_app_misw.data.repositories.UserRepository
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    private lateinit var userViewModel: UserViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        val application = mockk<Application>(relaxed = true)
        userViewModel = UserViewModel(application,userRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveUser should call repository to save the user`() = runBlockingTest {
        // Given
        val user = Usuario(
            tipoUsuario = TipoUsuario.USUARIO
        )

        // When
        userViewModel.saveUser(user)

        // Then
        verify { userRepository.saveUser(user) }
    }

    @Test
    fun `loadUser should retrieve user from repository and set LiveData`() = runBlockingTest {
        // Given
        val user = Usuario(
            tipoUsuario = TipoUsuario.USUARIO
        )
        every { userRepository.getUser() } returns user

        // When
        userViewModel.loadUser()

        // Then
        assertEquals(user, userViewModel.user.value)
    }

    @Test
    fun `loadUser should set user LiveData to null when no user is found`() = runBlockingTest {
        // Given
        every { userRepository.getUser() } returns null

        // When
        userViewModel.loadUser()

        // Then
        assertEquals(null, userViewModel.user.value)
    }

    @Test
    fun `clearUser should call repository to clear user and set LiveData to null`() = runBlockingTest {
        // When
        userViewModel.clearUser()

        // Then
        verify { userRepository.clearUser() }
        assertEquals(null, userViewModel.user.value)
    }
}
