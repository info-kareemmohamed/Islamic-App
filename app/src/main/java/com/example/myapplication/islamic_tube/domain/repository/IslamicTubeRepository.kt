package com.example.myapplication.islamic_tube.domain.repository

import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.Result
import com.example.myapplication.islamic_tube.domain.model.Category

interface IslamicTubeRepository {
    suspend fun getIslamicTubeVideos(): Result<List<Category>, NetworkError>
}