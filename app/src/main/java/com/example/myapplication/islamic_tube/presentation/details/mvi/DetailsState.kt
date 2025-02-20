package com.example.myapplication.islamic_tube.presentation.details.mvi

import com.example.myapplication.islamic_tube.domain.model.Video

data class DetailsState(
    val isLoading: Boolean = false,
    val isCategoryListDialogVisible: Boolean = false,
    val isCreateCategoryDialogVisible: Boolean = false,
    val currentVideo: Video = Video(title = "", url = ""),
    val currentCategory: String = "",
    val savedCategoryNames: List<String> = emptyList(),
    val relatedVideos: List<Video> = emptyList(),
    val videoCategoryNames: List<String> = emptyList()
)
