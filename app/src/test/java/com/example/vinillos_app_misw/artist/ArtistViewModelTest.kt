package com.example.vinillos_app_misw.artist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.repositories.ArtistRepository
import com.example.vinillos_app_misw.presentation.view_model.artist.ArtistViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

class ArtistViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var artistRepository: ArtistRepository

    private lateinit var viewModel: ArtistViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ArtistViewModel(artistRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getArtists should post artist list on success`() = runTest {
        val mockArtist1 = Performer(
            id = 1,
            name = "Artist One",
            image = "image1.png",
            description = "Description1",
            birthDate = "1990-01-01",
            albumId = 0,
            collectorId = 0
        )
        val mockArtist2 = Performer(
            id = 2,
            name = "Artist Two",
            image = "image2.png",
            description = "Description2",
            birthDate = "1992-02-02",
            albumId = 0,
            collectorId = 0
        )
        val mockArtists = listOf(mockArtist1, mockArtist2)

        // Given
        coEvery { artistRepository.getArtists() } returns mockArtists

        // When
        val observer = mockk<Observer<List<Performer>>>(relaxed = true)
        viewModel.artists.observeForever(observer)
        viewModel.getArtists()

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(mockArtists) }
    }

    @Test
    fun `getArtists should post error message on failure`() = runTest {
        val errorMessage = "Network Error"

        // Given
        coEvery { artistRepository.getArtists() } throws RuntimeException(errorMessage)

        // When
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)
        viewModel.getArtists()

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(errorMessage) }
    }

    @Test
    fun `getArtist should post artist on success`() = runTest {
        val mockArtist = Performer(
            id = 1,
            name = "Artist One",
            image = "image.png",
            description = "Description",
            birthDate = "1990-01-01",
            albumId = 0,
            collectorId = 0
        )

        // Given
        coEvery { artistRepository.getArtist(mockArtist.id) } returns mockArtist

        // When
        val observer = mockk<Observer<Performer>>(relaxed = true)
        viewModel.artist.observeForever(observer)
        viewModel.getArtist(mockArtist.id)

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(mockArtist) }
    }

    @Test
    fun `getArtist should post error message on failure`() = runTest {
        val artistId = 1
        val errorMessage = "Artist not found"

        // Given
        coEvery { artistRepository.getArtist(artistId) } throws RuntimeException(errorMessage)

        // When
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)
        viewModel.getArtist(artistId)

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(errorMessage) }
    }

    @Test
    fun `saveArtistID should call repository to save artist ID`() = runTest {
        // Given
        val artistId = 42

        // When
        viewModel.saveArtistID(artistId)

        // Then
        verify { artistRepository.saveArtistID(artistId) }
    }

    @Test
    fun `getArtistId should retrieve artist ID from repository`() = runTest {
        // Given
        val expectedArtistId = 42
        coEvery { artistRepository.getArtistId() } returns expectedArtistId

        // When
        viewModel.getArtistId()

        // Then
        Assert.assertEquals(expectedArtistId, viewModel.artistId.value)
    }

    @Test
    fun `clearArtistID should clear the artist ID in repository and set LiveData to null`() = runTest {
        // When
        viewModel.clearArtistID()

        // Then
        verify { artistRepository.clearArtistID() }
        Assert.assertEquals(null, viewModel.artistId.value)
    }
}
