package com.example.myapplication.islamic_tube.presentation.details.mvi

import com.example.myapplication.islamic_tube.domain.model.Video

sealed class DetailsIntent {
    data class LoadDataFromLocal(val video: Video, val categoryName: String) : DetailsIntent()
    data class LoadDataFromNetwork(
        val video: Video,
        val categoryName: String,
        val subCategoryName: String
    ) : DetailsIntent()

    data class ToggleCategoryListDialog(val isVisible: Boolean) : DetailsIntent()
    data class ToggleCreateCategoryDialog(val isVisible: Boolean) : DetailsIntent()
    data class CreateCategory(val categoryName: String) : DetailsIntent()
    data class SaveVideo(val video: Video, val categories: List<String>) : DetailsIntent()
}
