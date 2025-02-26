package com.example.myapplication.islamic_tube.presentation.details.mvi

import com.example.myapplication.islamic_tube.domain.model.Video

sealed class DetailsIntent {
    data class LoadPlaylist(val isFromFavorite: Boolean, val playlistName: String) : DetailsIntent()
    data class ClickVideo(val video: Video) : DetailsIntent()
    data class ToggleCategoryListDialog(val isVisible: Boolean) : DetailsIntent()
    data class ToggleCreateCategoryDialog(val isVisible: Boolean) : DetailsIntent()
    data class CreateCategory(val categoryName: String) : DetailsIntent()
    data class SaveVideo(val categories: List<String>) : DetailsIntent()
}
