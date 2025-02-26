package com.example.myapplication.islamic_tube.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.domain.onError
import com.example.myapplication.core.domain.onSuccess
import com.example.myapplication.islamic_tube.domain.model.Section
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val islamicTubeRepository: IslamicTubeRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _categories = MutableStateFlow<List<Section>>(emptyList())
    val categories: StateFlow<List<Section>> = _categories
        .onStart { loadIslamicTubeVideos() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _errorMessage = Channel<NetworkError>()
    val errorMessage = _errorMessage.receiveAsFlow()

    fun loadIslamicTubeVideos() {
        viewModelScope.launch {
            _isLoading.value = true

            islamicTubeRepository.getSections()
                .onSuccess { categories ->
                    _categories.update { categories }
                    _isLoading.value = false
                }
                .onError { error ->
                    _errorMessage.send(error)
                    _isLoading.value = false
                }
        }
    }
}
