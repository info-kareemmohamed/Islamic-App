package com.example.myapplication.islamic_tube.data.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.core.data.networking.safeCall
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.core.domain.map
import com.example.myapplication.islamic_tube.data.local.CategoryDao
import com.example.myapplication.islamic_tube.data.mappres.toCategories
import com.example.myapplication.islamic_tube.data.mappres.toVideo
import com.example.myapplication.islamic_tube.data.mappres.toVideoEntity
import com.example.myapplication.islamic_tube.data.networking.dto.IslamicTubeDto
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.SubCategory
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IslamicTubeRepositoryImpl(
    private val httpClient: HttpClient,
    private val categoryDao: CategoryDao,
) : IslamicTubeRepository {
    val categories = mutableListOf<Category>()

    override suspend fun getIslamicTubeVideos(): Result<List<Category>, NetworkError> {
        val result = safeCall<IslamicTubeDto> {
            httpClient.get(urlString = BuildConfig.API_BASE_URL)
        }
        return result.map { dto ->
            val categoriesList = dto.toCategories()
            categories.clear()
            categories.addAll(categoriesList)
            categoriesList
        }
    }

    override suspend fun getSubCategoryFromNetwork(
        categoryName: String,
        subCategoryName: String
    ): Result<SubCategory, NetworkError> =
        categories.find { it.name == categoryName }
            ?.subCategories?.find { it.name == subCategoryName }
            ?.let { Result.Success(it) }
            ?: Result.Error(NetworkError.UNKNOWN)


    override suspend fun getSubCategoryFromLocal(categoryName: String): List<Video> =
        categoryDao.getCategoryByName(categoryName)?.videoEntities?.map { it.toVideo() }
            ?: emptyList()

    override fun observeCategoryNamesAndFirstVideo(): Flow<Pair<List<Video>, List<String>>> =
        categoryDao.observeCategoryNameAndFirstVideo().map { list ->
            val names = list.map { it.name }
            val videos = list.map { it.firstVideo.toVideo() }
            videos to names
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