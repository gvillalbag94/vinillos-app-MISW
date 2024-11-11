package com.example.vinillos_app_misw.collector

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.repositories.CollectorRepository
import com.example.vinillos_app_misw.presentation.view_model.collector.CollectorViewModel
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


class CollectorViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var collectorRepository: CollectorRepository

    private lateinit var viewModel: CollectorViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CollectorViewModel(collectorRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getCollectors should post collector list on success`() = runTest {
        val mockCollector1 = Collector(
            id = 1,
            name = "John Doe",
            telephone = "123-456-7890",
            email = "johndoe@example.com",
            comments = emptyList(),
            favoritePerformers = emptyList(),
            collectorAlbums = emptyList()
        )

        val mockCollector2 = Collector(
            id = 2,
            name = "John Doe",
            telephone = "123-456-7890",
            email = "johndoe@example.com",
            comments = emptyList(),
            favoritePerformers = emptyList(),
            collectorAlbums = emptyList()
        )

        val mockCollectors = listOf(mockCollector1, mockCollector2)

        // Given
        coEvery { collectorRepository.getCollectors() } returns mockCollectors

        // when
        val observer = mockk<Observer<List<Collector>>>(relaxed = true)
        viewModel.collectors.observeForever(observer)
        viewModel.getCollectors()

        advanceUntilIdle()

        // then
        verify { observer.onChanged(mockCollectors) }
    }

    @Test
    fun `getCollectors should post error message on failure`() = runTest {
        val errorMessage = "Network Error"

        // Given
        coEvery { collectorRepository.getCollectors() } throws RuntimeException(errorMessage)

        // When
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)
        viewModel.getCollectors()

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(errorMessage) }
    }

    @Test
    fun `getCollector should post collector on success`() = runTest {
        val mockCollector = Collector(
            id = 1,
            name = "John Doe",
            telephone = "123-456-7890",
            email = "johndoe@example.com",
            comments = emptyList(),
            favoritePerformers = emptyList(),
            collectorAlbums = emptyList()
        )

        // Given
        coEvery { collectorRepository.getCollector(mockCollector.id) } returns mockCollector

        // When
        val observer = mockk<Observer<Collector>>(relaxed = true)
        viewModel.collector.observeForever(observer)
        viewModel.getCollector(mockCollector.id)

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(mockCollector) }
    }

    @Test
    fun `getCollector should post error message on failure`() = runTest {
        val collectorId = 1
        val errorMessage = "Collector not found"

        // Given
        coEvery { collectorRepository.getCollector(collectorId) } throws RuntimeException(errorMessage)

        // When
        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)
        viewModel.getCollector(collectorId)

        advanceUntilIdle()

        // Then
        verify { observer.onChanged(errorMessage) }
    }
}
