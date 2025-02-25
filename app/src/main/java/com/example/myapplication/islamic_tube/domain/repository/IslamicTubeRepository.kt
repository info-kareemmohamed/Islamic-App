package com.example.myapplication.islamic_tube.domain.repository

import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.SubCategory
import com.example.myapplication.islamic_tube.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface IslamicTubeRepository {
    suspend fun getIslamicTubeVideos(): Result<List<Category>, NetworkError>

    suspend fun getSubCategoryFromNetwork(
        categoryName: String,
        subCategoryName: String
    ):  Result<SubCategory, NetworkError>

    suspend fun getSubCategoryFromLocal(categoryName: String): List<Video>

    fun observeCategoryNamesAndFirstVideo(): Flow<Pair<List<Video>, List<String>>>

    fun observeCategoryNamesByVideoUrl(videoUrl: String): Flow<List<String>>

    suspend fun createEmptyCategory(categoryName: String)

    fun observeCategoryNames(): Flow<List<String>>

    suspend fun upsertVideoCategories(
        oldCategoryNames: List<String>, newCategoryNames: List<String>,
        video: Video
    )
}