package com.example.myapplication.islamic_tube.data.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.core.data.networking.safeCall
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.core.domain.map
import com.example.myapplication.islamic_tube.data.local.CategoryDao
import com.example.myapplication.islamic_tube.data.mappres.toCategory
import com.example.myapplication.islamic_tube.data.mappres.toPlaylist
import com.example.myapplication.islamic_tube.data.mappres.toSections
import com.example.myapplication.islamic_tube.data.mappres.toVideoEntity
import com.example.myapplication.islamic_tube.data.networking.dto.IslamicTubeDto
import com.example.myapplication.islamic_tube.data.networking.dto.ItemDto
import com.example.myapplication.islamic_tube.data.networking.dto.SectionDto
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
    // TODO: Remove this temporary cache once the API is ready.
    val sections = mutableListOf<SectionDto>()

    override suspend fun getSections(): Result<List<Section>, NetworkError> {
        val result = safeCall<IslamicTubeDto> {
            httpClient.get(urlString = "${BuildConfig.API_BASE_URL}data.json")
        }
        // TODO: Replace cached logic with direct API call when available.
        return result.map { dto ->
            val sectionsList = dto.sections
            sections.apply {
                clear()
                addAll(sectionsList)
            }
            dto.toSections()
        }
    }

    override suspend fun getPlaylist(
        playlistName: String
    ): Result<Playlist, NetworkError> {
        // TODO: Replace cached logic with direct API call when available.
        val playlistId = sections
            .flatMap { it.categories }
            .find { it.title == playlistName }
            ?.playlistId.orEmpty()

        if (playlistId.isEmpty()) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return safeCall<List<ItemDto>> {
            httpClient.get(urlString = "${BuildConfig.API_BASE_URL}playlists/$playlistId.json")
        }.map { items ->
            items.toPlaylist(playlistName)
        }
    }

    override suspend fun searchCategoryList(query: String): Result<List<Category>, NetworkError> =
        // TODO: Replace cached logic with direct API call when available.
        sections.flatMap { section ->
            section.categories.filter { category ->
                category.title.contains(query, ignoreCase = true)
            }
        }.takeIf { it.isNotEmpty() }
            ?.let { Result.Success(it.map { it.toCategory() }) }
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