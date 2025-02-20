package com.example.myapplication.islamic_tube.presentation.details

import app.cash.turbine.test
import com.example.TestCoroutineRule
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.SubCategory
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import com.example.myapplication.islamic_tube.presentation.details.mvi.DetailsIntent
import com.example.myapplication.islamic_tube.presentation.details.mvi.DetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private lateinit var repo: IslamicTubeRepository
    private lateinit var viewModel: DetailsViewModel

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        // GIVEN: a mocked repository and initial state
        repo = mockk(relaxed = true)
        // Stub observeCategoryNames() to return an empty list for simplicity
        every { repo.observeCategoryNames() } returns flowOf(emptyList())
        viewModel = DetailsViewModel(repo)
    }

    @Test
    fun `given successful network response when LoadDataFromNetwork intent sent then state is updated`() =
        runTest {
            // GIVEN: test video, category, subcategory, and expected related videos.
            val testVideo = Video(title = "Test Video", url = "http://test.com/video")
            val categoryName = "TestCategory"
            val subCategoryName = "SubCategory"
            val expectedVideos =
                listOf(Video(title = "Related 1", url = "http://test.com/related1"))
            val dummySubCategory = SubCategory(videos = expectedVideos, name = subCategoryName)

            // Stub network call to return success and stub video URL observation.
            coEvery {
                repo.getSubCategoryFromNetwork(
                    categoryName,
                    subCategoryName
                )
            } returns Result.Success(dummySubCategory)
            coEvery { repo.observeCategoryNamesByVideoUrl(testVideo.url) } returns flowOf(
                listOf(
                    "Category1",
                    "Category2"
                )
            )

            // WHEN: LoadDataFromNetwork intent is sent.
            viewModel.onIntent(
                DetailsIntent.LoadDataFromNetwork(
                    testVideo,
                    categoryName,
                    subCategoryName
                )
            )

            // THEN: Verify state updates using Turbine.
            viewModel.state.test {
                // First emission: initial state.
                awaitItem()
                // Next: loading state with currentVideo, currentCategory set and isLoading true.
                val loadingState = awaitItem()
                check(loadingState.currentVideo == testVideo)
                check(loadingState.currentCategory == categoryName)
                check(loadingState.isLoading)

                // Next: success state with relatedVideos updated and isLoading false.
                val successState = awaitItem()
                check(successState.relatedVideos == expectedVideos)
                check(!successState.isLoading)

                // Finally: state with updated videoCategoryNames and currentVideo.
                val finalState = awaitItem()
                check(finalState.videoCategoryNames == listOf("Category1", "Category2"))
                check(finalState.currentVideo == testVideo)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given local data when LoadDataFromLocal intent is sent then final state updates with related videos`() =
        runTest {
            // GIVEN: a test video, category name, and expected related videos.
            val testVideo = Video(title = "Test Video", url = "http://test.com/video")
            val categoryName = "LocalCategory"
            val expectedRelatedVideos = listOf(
                Video(title = "Local Related", url = "http://test.com/localrelated")
            )
            // Stub the repository call to return expected related videos.
            coEvery { repo.getSubCategoryFromLocal(categoryName) } returns expectedRelatedVideos

            // WHEN: the LoadDataFromLocal intent is sent.
            viewModel.onIntent(DetailsIntent.LoadDataFromLocal(testVideo, categoryName))

            // THEN: verify that the final state reflects the loaded data.
            viewModel.state.test {
                // The first emission is the initial state.
                awaitItem()
                awaitItem()

                // Next emission: the final state with currentVideo, currentCategory, relatedVideos updated and isLoading false.
                val finalState = awaitItem()
                // Check that the final state matches our expectations.
                assertEquals(testVideo, finalState.currentVideo)
                assertEquals(categoryName, finalState.currentCategory)
                assertEquals(expectedRelatedVideos, finalState.relatedVideos)
                assertFalse(finalState.isLoading)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given SaveVideo intent when processed then repository upsertVideoCategories is called with correct parameters`() =
        runTest {
            // GIVEN: a test video and a list of new category names.
            val testVideo = Video(title = "Test Video", url = "http://test.com/video")
            val newCategories = listOf("Cat1", "Cat2")

            // The initial state's videoCategoryNames is empty by default.
            // WHEN: The SaveVideo intent is sent.
            viewModel.onIntent(DetailsIntent.SaveVideo(testVideo, newCategories))

            // Ensure that the coroutine launched in saveVideo() completes.
            advanceUntilIdle()

            // THEN: Verify that the repository method is called with the correct parameters.
            coVerify {
                repo.upsertVideoCategories(
                    newCategoryNames = newCategories,
                    video = testVideo,
                    oldCategoryNames = emptyList() // from initial state
                )
            }
        }
}

