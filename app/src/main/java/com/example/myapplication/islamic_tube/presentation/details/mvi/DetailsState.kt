package com.example.myapplication.islamic_tube.presentation.details.mvi

import com.example.myapplication.islamic_tube.domain.model.Playlist
import com.example.myapplication.islamic_tube.domain.model.Video

data class DetailsState(
    val isLoading: Boolean = false,
    val isCategoryListDialogVisible: Boolean = false,
    val isCreateCategoryDialogVisible: Boolean = false,
    val currentVideo: Video = Video(title = "", url = ""),
    val playlist: Playlist = Playlist(name = "", videos = emptyList()),
    val savedCategoryNames: List<String> = emptyList(),
    val videoCategoryNames: List<String> = emptyList()
)
