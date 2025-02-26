package com.example.myapplication.islamic_tube.presentation.details

import app.cash.turbine.test
import com.example.TestCoroutineRule
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.Playlist
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
        // Stub global category names to return an empty list for simplicity
        every { repo.observeCategoryNames() } returns flowOf(emptyList())
        viewModel = DetailsViewModel(repo)
    }

    @Test
    fun `when network playlist is loaded then state is updated`() = runTest {
        // GIVEN: a test playlist with a first video
        val firstVideo = Video(title = "First Video", url = "http://test.com/first")
        val testPlaylist = Playlist(
            name = "TestPlaylist",
            videos = listOf(
                firstVideo,
                Video(title = "Second Video", url = "http://test.com/second")
            )
        )
        // Stub network call to return success for the playlist
        coEvery { repo.getPlaylist("TestPlaylist") } returns Result.Success(testPlaylist)

        // WHEN: a LoadPlaylist intent is sent for network load (isFromFavorite = false)
        viewModel.onIntent(
            DetailsIntent.LoadPlaylist(
                isFromFavorite = false,
                playlistName = "TestPlaylist"
            )
        )

        // THEN: state should be updated with the test playlist and currentVideo set to the first video
        viewModel.state.test {
            // First emission: initial state
            awaitItem()
            awaitItem()
            // Next emission: state after loading
            val stateAfterLoad = awaitItem()
            assertEquals(testPlaylist, stateAfterLoad.playlist)
            assertEquals(firstVideo, stateAfterLoad.currentVideo)
            assertFalse(stateAfterLoad.isLoading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when local playlist is loaded then state is updated`() = runTest {
        // GIVEN: a test local playlist with a first video
        val firstVideo = Video(title = "Local First Video", url = "http://test.com/localfirst")
        val testPlaylist = Playlist(
            name = "LocalPlaylist",
            videos = listOf(
                firstVideo,
                Video(title = "Local Second Video", url = "http://test.com/localsecond")
            )
        )
        // Stub local call to return the test playlist as a Flow
        coEvery { repo.observePlaylistFromLocal("LocalPlaylist") } returns flowOf(testPlaylist)

        // WHEN: a LoadPlaylist intent is sent for local load (isFromFavorite = true)
        viewModel.onIntent(
            DetailsIntent.LoadPlaylist(
                isFromFavorite = true,
                playlistName = "LocalPlaylist"
            )
        )

        // THEN: state should be updated with the test playlist and currentVideo set to the first video (if not already set)
        viewModel.state.test {
            // First emission: initial state
            awaitItem()
            awaitItem()
            // Next emission: state after loading
            val stateAfterLoad = awaitItem()
            assertEquals(testPlaylist, stateAfterLoad.playlist)
            // If currentVideo was not set before, it should be updated to firstVideo
            assertEquals(firstVideo, stateAfterLoad.currentVideo)
            assertFalse(stateAfterLoad.isLoading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when SaveVideo intent is processed then repository upsertVideoCategories is called`() =
        runTest {
            // GIVEN: a test video and a new category list; set currentVideo to the test video
            val testVideo = Video(title = "Test Video", url = "http://test.com/video")
            viewModel.onIntent(DetailsIntent.ClickVideo(testVideo))
            val newCategories = listOf("Cat1", "Cat2")

            // WHEN: a SaveVideo intent is sent
            viewModel.onIntent(DetailsIntent.SaveVideo(newCategories))
            advanceUntilIdle()

            // THEN: verify that repository.upsertVideoCategories is called with correct parameters
            coVerify {
                repo.upsertVideoCategories(
                    newCategoryNames = newCategories,
                    video = testVideo,
                    oldCategoryNames = emptyList() // initial state's videoCategoryNames is empty
                )
            }
        }
}
