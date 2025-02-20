package com.example.myapplication.islamic_tube.presentation.home

import app.cash.turbine.test
import com.example.TestCoroutineRule
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun `given repository returns success when loadIslamicTubeVideos is called then categories are updated and isLoading is false`() =
        runTest {
            // Given: Mock repository returning a list of categories successfully
            val expectedCategories = listOf(Category("1", subCategories = emptyList()))
            val repository = mockk<IslamicTubeRepository>()
            coEvery { repository.getIslamicTubeVideos() } returns Result.Success(expectedCategories)

            val viewModel = HomeViewModel(repository)

            // When: Call loadIslamicTubeVideos to load videos
            viewModel.loadIslamicTubeVideos()

            // Then: Categories should be updated, and isLoading should be false
            viewModel.categories.test {
                awaitItem() // Skip initial value
                assertEquals(expectedCategories, awaitItem()) // Verify updated categories
            }
            assertFalse(viewModel.isLoading.value) // Verify loading is false
        }

    @Test
    fun `given repository returns error when loadIslamicTubeVideos is called then error message is emitted and isLoading is false`() =
        runTest {
            // Given: Mock repository returns an error
            val fakeError = NetworkError.NO_INTERNET
            val repository = mockk<IslamicTubeRepository> {
                coEvery { getIslamicTubeVideos() } returns Result.Error(fakeError)
            }
            val viewModel = HomeViewModel(repository)

            // When: Trigger video loading
            viewModel.loadIslamicTubeVideos()


            // Then: Verify error emission and loading state
            viewModel.errorMessage.test {
                assertEquals(fakeError, awaitItem()) // Verify emitted error
                cancelAndIgnoreRemainingEvents()
            }
            assertFalse(viewModel.isLoading.value) // Loading should be false
        }
}
