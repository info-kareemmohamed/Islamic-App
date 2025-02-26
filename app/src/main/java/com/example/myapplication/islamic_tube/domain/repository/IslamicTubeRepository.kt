package com.example.myapplication.islamic_tube.domain.repository

import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.model.Playlist
import com.example.myapplication.islamic_tube.domain.model.Section
import com.example.myapplication.islamic_tube.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface IslamicTubeRepository {
    suspend fun getSections(): Result<List<Section>, NetworkError>

    suspend fun getPlaylist(playlistName: String): Result<Playlist, NetworkError>

    suspend fun searchCategoryList(query: String): Result<List<Category>, NetworkError>

    fun observePlaylistFromLocal(playlistName: String): Flow<Playlist>

    fun observeCategoryList(): Flow<List<Category>>

    fun observeCategoryNamesByVideoUrl(videoUrl: String): Flow<List<String>>

    suspend fun createEmptyCategory(categoryName: String)

    fun observeCategoryNames(): Flow<List<String>>

    suspend fun upsertVideoCategories(
        oldCategoryNames: List<String>,
        newCategoryNames: List<String>,
        video: Video
    )
}