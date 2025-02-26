package com.example.myapplication.islamic_tube.presentation.home

import app.cash.turbine.test
import com.example.TestCoroutineRule
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.Section
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
    fun `given repository returns success when loadIslamicTubeVideos is called then sections are updated and isLoading is false`() =
        runTest {
            // GIVEN: A mocked repository returning a list of Section objects successfully.
            val expectedSections = listOf(
                Section(name = "Section 1", categories = emptyList())
            )
            val repository = mockk<IslamicTubeRepository>()
            coEvery { repository.getSections() } returns Result.Success(expectedSections)

            val viewModel = HomeViewModel(repository)

            // WHEN: Calling loadIslamicTubeVideos() to load data.
            viewModel.loadIslamicTubeVideos()

            // THEN: The sections state should be updated and isLoading should be false.
            viewModel.categories.test {
                awaitItem() // Skip initial emission (emptyList)
                assertEquals(expectedSections, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
            assertFalse(viewModel.isLoading.value)
        }

    @Test
    fun `given repository returns error when loadIslamicTubeVideos is called then error message is emitted and isLoading is false`() =
        runTest {
            // GIVEN: A mocked repository that returns an error.
            val fakeError = NetworkError.NO_INTERNET
            val repository = mockk<IslamicTubeRepository> {
                coEvery { getSections() } returns Result.Error(fakeError)
            }
            val viewModel = HomeViewModel(repository)

            // WHEN: Triggering the loadIslamicTubeVideos() method.
            viewModel.loadIslamicTubeVideos()

            // THEN: Verify that an error message is emitted and loading state is false.
            viewModel.errorMessage.test {
                assertEquals(fakeError, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            assertFalse(viewModel.isLoading.value)
        }
}