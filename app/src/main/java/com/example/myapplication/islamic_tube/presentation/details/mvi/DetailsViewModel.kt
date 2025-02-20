package com.example.myapplication.islamic_tube.presentation.details.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.onError
import com.example.myapplication.core.domain.onSuccess
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repo: IslamicTubeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    private val _errorMessage = Channel<NetworkError>()
    val errorMessage = _errorMessage.receiveAsFlow()

    init {
        viewModelScope.launch {
            repo.observeCategoryNames().collect { categories ->
                _state.update { it.copy(savedCategoryNames = categories) }
            }
        }
    }

    fun onIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.ToggleCategoryListDialog ->
                _state.update { it.copy(isCategoryListDialogVisible = intent.isVisible) }

            is DetailsIntent.ToggleCreateCategoryDialog ->
                _state.update { it.copy(isCreateCategoryDialogVisible = intent.isVisible) }

            is DetailsIntent.LoadDataFromLocal -> loadLocalData(intent.video, intent.categoryName)
            is DetailsIntent.LoadDataFromNetwork -> loadNetworkData(
                intent.video,
                intent.categoryName,
                intent.subCategoryName
            )

            is DetailsIntent.CreateCategory -> createCategory(intent.categoryName)
            is DetailsIntent.SaveVideo -> saveVideo(intent.video, intent.categories)
        }
    }

    private fun loadLocalData(video: Video, categoryName: String) {
        _state.update {
            it.copy(
                currentCategory = categoryName,
                currentVideo = video,
                isLoading = true
            )
        }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    relatedVideos = repo.getSubCategoryFromLocal(categoryName),
                    isLoading = false
                )
            }
        }
    }

    private fun loadNetworkData(video: Video, categoryName: String, subCategoryName: String) =
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentVideo = video,
                    currentCategory = categoryName,
                    isLoading = true
                )
            }

            repo.getSubCategoryFromNetwork(categoryName, subCategoryName)
                .onSuccess { subCat ->
                    _state.update { it.copy(relatedVideos = subCat.videos, isLoading = false) }
                }
                .onError { error ->
                    _errorMessage.send(error)
                    _state.update { it.copy(isLoading = false) }
                }

            repo.observeCategoryNamesByVideoUrl(video.url).collect { names ->
                _state.update { it.copy(videoCategoryNames = names) }
            }
        }

    private fun createCategory(categoryName: String) = viewModelScope.launch {
        repo.createEmptyCategory(categoryName)
        _state.update { it.copy(isCreateCategoryDialogVisible = false) }
    }

    private fun saveVideo(video: Video, categories: List<String>) = viewModelScope.launch {
        repo.upsertVideoCategories(
            newCategoryNames = categories,
            video = video,
            oldCategoryNames = _state.value.videoCategoryNames
        )
    }
}
