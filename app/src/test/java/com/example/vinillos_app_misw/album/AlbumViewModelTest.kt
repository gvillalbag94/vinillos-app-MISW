package com.example.vinillos_app_misw.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class AlbumViewModelTest {

    @RelaxedMockK
    private lateinit var albumRepository: AlbumRepository

    private lateinit var viewModel: AlbumViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = AlbumViewModel(albumRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAlbums should update albums LiveData on success`() {
        //Given
        val albumList = listOf(Album(
            id = 100,
            name = "Buscando América",
            cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Buscando América es el primer álbum de la banda de Rubén Blades...",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList()
        ), Album(
            id = 101,
            name = "Buscando América",
            cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Buscando América es el primer álbum de la banda de Rubén Blades...",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList()
        ))

        every { albumRepository.getAlbums(any(), any()) } answers {
            val successCallback = firstArg<(List<Album>) -> Unit>()
            successCallback(albumList)
        }

        // When
        viewModel.getAlbums()

        // Expected
        viewModel.albums.observeForever { actualAlbums ->
            assertEquals(albumList, actualAlbums)
        }
    }

    @Test
    fun `getAlbums should set error when failure occurs`() = runBlockingTest {
        // Given
        val errorMessage = "Error fetching albums"


        every { albumRepository.getAlbums(any(), any()) } answers {
            val errorCallback = secondArg<(String) -> Unit>()
            errorCallback(errorMessage)
        }

        // When
        viewModel.getAlbums()

        // Then
        assertEquals(errorMessage, viewModel.error.value)
    }

    @Test
    fun `getAlbum should set album when successful`() = runBlockingTest {
        // Given
        val albumToRetrieve = Album(
            id = 1,
            name = "Test Album",
            cover = "",
            releaseDate = "",
            description = "",
            genre = "",
            recordLabel = "",
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList(),
            )

        every { albumRepository.getAlbum(1, any(), any()) } answers {
            val successCallback = secondArg<(Album) -> Unit>()
            successCallback(albumToRetrieve)
        }

        // When
        viewModel.getAlbum(1)

        // Then
        assertEquals(albumToRetrieve, viewModel.album.value)
    }

    @Test
    fun `getAlbum should set error when album not found`() = runBlockingTest {
        // Given
        val errorMessage = "Album not found"
        every { albumRepository.getAlbum(1, any(), any()) } answers {
            val errorCallback = thirdArg<(String) -> Unit>()
            errorCallback(errorMessage)
        }

        // When
        viewModel.getAlbum(1)

        // Then
        assertEquals(errorMessage, viewModel.error.value)
    }

    @Test
    fun `saveAlbumID should call repository to save album ID`() = runBlockingTest {
        // Given
        val albumId = 42

        // When
        viewModel.saveAlbumID(albumId)

        // Then
        verify { albumRepository.saveAlbumID(albumId) }
    }

    @Test
    fun `getAlbumId should retrieve album ID from repository`() = runBlockingTest {
        // Given
        val expectedAlbumId = 42
        every { albumRepository.getAlbumId() } returns expectedAlbumId

        // When
        viewModel.getAlbumId()

        // Then
        assertEquals(expectedAlbumId, viewModel.albumId.value)
    }

    @Test
    fun `clearAlbumID should clear the album ID in repository and set LiveData to null`() = runBlockingTest {
        // When
        viewModel.clearAlbumID()

        // Then
        verify { albumRepository.clearAlbumID() } // Check if the repository method was called
        assertEquals(null, viewModel.albumId.value) // Ensure the LiveData has been set to null
    }
}