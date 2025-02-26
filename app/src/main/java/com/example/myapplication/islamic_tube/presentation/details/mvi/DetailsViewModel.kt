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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        // Collect global category names
        viewModelScope.launch {
            repo.observeCategoryNames().collect { categories ->
                _state.update { it.copy(savedCategoryNames = categories) }
            }
        }

        // Observe changes to the current video and update its category names
        _state.distinctUntilChangedBy { it.currentVideo }
            .onEach { observeVideoCategoryNames(_state.value.currentVideo.url) }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.ToggleCategoryListDialog ->
                _state.update { it.copy(isCategoryListDialogVisible = intent.isVisible) }

            is DetailsIntent.ToggleCreateCategoryDialog ->
                _state.update { it.copy(isCreateCategoryDialogVisible = intent.isVisible) }

            is DetailsIntent.ClickVideo ->
                _state.update { it.copy(currentVideo = intent.video) }

            is DetailsIntent.LoadPlaylist -> {
                if (intent.isFromFavorite) loadLocalPlaylist(intent.playlistName)
                else loadNetworkPlaylist(intent.playlistName)
            }

            is DetailsIntent.CreateCategory -> createCategory(intent.categoryName)
            is DetailsIntent.SaveVideo -> saveVideo(_state.value.currentVideo, intent.categories)
        }
    }


    private fun loadLocalPlaylist(playlistName: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        repo.observePlaylistFromLocal(playlistName).collectLatest { playlist ->
            _state.update {
                it.copy(
                    playlist = playlist,
                    currentVideo = if (it.currentVideo.url.isEmpty()) playlist.videos.first() else it.currentVideo,
                    isLoading = false
                )
            }
        }
    }


    private fun loadNetworkPlaylist(playlistName: String) =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            repo.getPlaylist(playlistName)
                .onSuccess { playlist ->
                    _state.update {
                        it.copy(
                            playlist = playlist,
                            currentVideo = playlist.videos.first(),
                            isLoading = false
                        )
                    }
                }
                .onError { error ->
                    _errorMessage.send(error)
                    _state.update { it.copy(isLoading = false) }
                }
        }

    private fun createCategory(categoryName: String) = viewModelScope.launch {
        repo.createEmptyCategory(categoryName)
        _state.update { it.copy(isCreateCategoryDialogVisible = false) }
    }


    // Launch a separate flow to observe category names for the video's URL.
    private fun observeVideoCategoryNames(videoUrl: String) =
        repo.observeCategoryNamesByVideoUrl(videoUrl)
            .onEach { names ->
                _state.update { it.copy(videoCategoryNames = names) }
            }.launchIn(viewModelScope)


    private fun saveVideo(video: Video, categories: List<String>) = viewModelScope.launch {
        repo.upsertVideoCategories(
            newCategoryNames = categories,
            video = video,
            oldCategoryNames = _state.value.videoCategoryNames
        )
    }
}
