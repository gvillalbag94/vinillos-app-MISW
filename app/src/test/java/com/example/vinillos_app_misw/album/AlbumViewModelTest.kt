package com.example.vinillos_app_misw.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = AlbumViewModel(albumRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getAlbums should update albums LiveData on success`() = runTest {
        // Given
        val albumList = listOf(
            AlbumWithDetails(Album(id = 100, name = "Buscando América", cover = "https://image.url", releaseDate = "1984-08-01", description = "Description", genre = "Salsa", recordLabel = "Elektra"), tracks = emptyList(), performers = emptyList(), comments = emptyList()),
            AlbumWithDetails(Album(id = 101, name = "Buscando América", cover = "https://image.url", releaseDate = "1984-08-01", description = "Description", genre = "Salsa", recordLabel = "Elektra"), tracks = emptyList(), performers = emptyList(), comments = emptyList())
        )

        // Given
        coEvery {  albumRepository.getAlbums() } returns albumList

        // when
        val observer = mockk<Observer<List<AlbumWithDetails>>>(relaxed = true)
        viewModel.albums.observeForever(observer)
        viewModel.getAlbums()

        advanceUntilIdle()

        // then
        verify { observer.onChanged(albumList) }
    }

    @Test
    fun `getAlbums should set error when failure occurs`() = runTest {
        val errorMessage = "Network Error"

        // Given
        coEvery { albumRepository.getAlbums() } throws RuntimeException(errorMessage)

        // When
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)
        viewModel.getAlbums()

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(errorMessage) }
    }

    @Test
    fun `getAlbum should set album when successful`() = runTest {
        // Given
        val albumToRetrieve = AlbumWithDetails(Album(id = 100, name = "Buscando América", cover = "https://image.url", releaseDate = "1984-08-01", description = "Description", genre = "Salsa", recordLabel = "Elektra"), tracks = emptyList(), performers = emptyList(), comments = emptyList())


        // Given
        coEvery { albumRepository.getAlbum(albumToRetrieve.album.id) } returns albumToRetrieve

        // When
        val observer = mockk<Observer<AlbumWithDetails>>(relaxed = true)
        viewModel.album.observeForever(observer)
        viewModel.getAlbum(albumToRetrieve.album.id)

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(albumToRetrieve) }
    }

    @Test
    fun `getAlbum should set error when album not found`() = runTest {
        // Given
        val errorMessage = "Album not found"
        val albumId = 1

        // Given
        coEvery { albumRepository.getAlbum(albumId) } throws RuntimeException(errorMessage)

        // When
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)
        viewModel.getAlbum(albumId)

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(errorMessage) }
    }

    @Test
    fun `saveAlbumID should call repository to save album ID`() = runTest {
        // Given
        val albumId = 42

        // When
        viewModel.saveAlbumID(albumId)

        // Then
        verify { albumRepository.saveAlbumID(albumId) }
    }

    @Test
    fun `getAlbumId should retrieve album ID from repository`() = runTest {
        // Given
        val expectedAlbumId = 42
        every { albumRepository.getAlbumId() } returns expectedAlbumId

        // When
        viewModel.getAlbumId()

        // Then
        assertEquals(expectedAlbumId, viewModel.albumId.value)
    }

    @Test
    fun `clearAlbumID should clear the album ID in repository and set LiveData to null`() = runTest {
        // When
        viewModel.clearAlbumID()

        // Then
        verify { albumRepository.clearAlbumID() }
        assertEquals(null, viewModel.albumId.value)
    }
}