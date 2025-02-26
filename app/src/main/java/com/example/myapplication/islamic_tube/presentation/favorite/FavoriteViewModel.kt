package com.example.myapplication.islamic_tube.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    repo: IslamicTubeRepository
) : ViewModel() {

    val categories: StateFlow<List<Category>> =
        repo.observeCategoryList()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}