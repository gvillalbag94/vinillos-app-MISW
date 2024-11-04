package com.example.vinillos_app_misw.user

import com.example.vinillos_app_misw.data.adapters.UserAdapter
import com.example.vinillos_app_misw.data.model.TipoUsuario
import com.example.vinillos_app_misw.data.model.Usuario
import com.example.vinillos_app_misw.data.repositories.UserRepository
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var userAdapter: UserAdapter
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userAdapter = mockk(relaxed = true)
        userRepository = UserRepository(userAdapter)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `saveUser should call saveUser on UserAdapter`() {
        // Given
        val user = Usuario(
            tipoUsuario = TipoUsuario.USUARIO
        )

        // When
        userRepository.saveUser(user)

        // Then
        verify { userAdapter.saveUser(user) }
    }

    @Test
    fun `getUser should call getUser on UserAdapter and return user`() {
        // Given
        val user = Usuario(
            tipoUsuario = TipoUsuario.USUARIO
        )

        every { userAdapter.getUser() } returns user

        // When
        val result = userRepository.getUser()

        // Then
        assertEquals(user, result)
        verify { userAdapter.getUser() }
    }

    @Test
    fun `getUser should return null when UserAdapter returns null`() {
        // Given
        every { userAdapter.getUser() } returns null

        // When
        val result = userRepository.getUser()

        // Then
        assertEquals(null, result)
        verify { userAdapter.getUser() }
    }

    @Test
    fun `clearUser should call clearUser on UserAdapter`() {
        // When
        userRepository.clearUser()

        // Then
        verify { userAdapter.clearUser() }
    }
}
