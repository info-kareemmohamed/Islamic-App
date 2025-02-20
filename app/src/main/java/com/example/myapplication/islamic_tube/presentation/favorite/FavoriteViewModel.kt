package com.example.myapplication.islamic_tube.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    private val repo: IslamicTubeRepository
) : ViewModel() {

    val categories: StateFlow<Pair<List<Video>, List<String>>> =
        repo.observeCategoryNamesAndFirstVideo()
            .onStart { repo.observeCategoryNamesAndFirstVideo() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Pair(emptyList(), emptyList())
            )
}