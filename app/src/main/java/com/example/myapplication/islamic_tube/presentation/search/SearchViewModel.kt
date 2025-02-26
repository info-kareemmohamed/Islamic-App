package com.example.myapplication.islamic_tube.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.domain.onSuccess
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: IslamicTubeRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    fun onSearch() {
        _isLoading.value = true
        viewModelScope.launch {
            repo.searchCategoryList(_searchQuery.value).onSuccess { categoryList ->
                _categoryList.value = categoryList
                _isLoading.value = false
            }
        }
    }

    fun searchQueryChange(searchQuery: String) {
        _searchQuery.value = searchQuery
    }


}