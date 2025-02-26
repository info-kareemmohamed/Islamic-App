package com.example.myapplication.islamic_tube.data.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.core.data.networking.safeCall
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.core.domain.map
import com.example.myapplication.islamic_tube.data.local.CategoryDao
import com.example.myapplication.islamic_tube.data.mappres.toPlaylist
import com.example.myapplication.islamic_tube.data.mappres.toPlaylists
import com.example.myapplication.islamic_tube.data.mappres.toSections
import com.example.myapplication.islamic_tube.data.mappres.toVideoEntity
import com.example.myapplication.islamic_tube.data.networking.dto.IslamicTubeDto
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.Playlist
import com.example.myapplication.islamic_tube.domain.model.Section
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import com.example.myapplication.islamic_tube.presentation.util.extractYoutubeVideoId
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IslamicTubeRepositoryImpl(
    private val httpClient: HttpClient,
    private val categoryDao: CategoryDao,
) : IslamicTubeRepository {
    val sections = mutableListOf<Section>()
    val playlists = mutableListOf<Playlist>()

    override suspend fun getSections(): Result<List<Section>, NetworkError> {
        val result = safeCall<IslamicTubeDto> {
            httpClient.get(urlString = BuildConfig.API_BASE_URL)
        }
        return result.map { dto ->
            val sectionsList = dto.toSections()
            val playlistsList = dto.toPlaylists()
            sections.apply {
                clear()
                addAll(sectionsList)
            }
            playlists.apply {
                clear()
                addAll(playlistsList)
            }
            sectionsList
        }
    }

    override suspend fun getPlaylist(
        playlistName: String
    ): Result<Playlist, NetworkError> =
        playlists.find { it.name == playlistName }?.let { Result.Success(it) }
            ?: Result.Error(NetworkError.UNKNOWN)

    override suspend fun searchCategoryList(query: String): Result<List<Category>, NetworkError> =
        sections.flatMap { section ->
            section.categories.filter { category ->
                category.name.contains(query, ignoreCase = true)
            }
        }.takeIf { it.isNotEmpty() }
            ?.let { Result.Success(it) }
            ?: Result.Error(NetworkError.UNKNOWN)

    override fun observePlaylistFromLocal(playlistName: String): Flow<Playlist> =
        categoryDao.observeCategoryByName(playlistName).map {
            it?.toPlaylist() ?: Playlist("", emptyList())
        }


    override fun observeCategoryList(): Flow<List<Category>> =
        categoryDao.observeCategorySummaries().map { summaries ->
            summaries.map { summary ->
                val imageUrl = summary.lastSavedVideoUrl.extractYoutubeVideoId()
                    ?.let { "https://img.youtube.com/vi/$it/0.jpg" } ?: ""
                Category(name = summary.name, imageUrl = imageUrl)
            }
        }

    override fun observeCategoryNamesByVideoUrl(videoUrl: String): Flow<List<String>> =
        categoryDao.observeCategoryNamesByVideoUrl(videoUrl)


    override suspend fun createEmptyCategory(categoryName: String) =
        categoryDao.createCategoryIfNotExists(categoryName)


    override fun observeCategoryNames(): Flow<List<String>> =
        categoryDao.observeCategoryNames()


    override suspend fun upsertVideoCategories(
        oldCategoryNames: List<String>,
        newCategoryNames: List<String>,
        video: Video
    ) = categoryDao.upsertVideoCategories(oldCategoryNames, newCategoryNames, video.toVideoEntity())

}