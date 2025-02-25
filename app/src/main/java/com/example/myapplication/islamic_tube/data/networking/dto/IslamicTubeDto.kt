package com.example.myapplication.islamic_tube.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class IslamicTubeDto(
    val sections: List<SectionDto>,
    val items: List<ItemDto>
)

@Serializable
data class SectionDto(
    val title: String,
    val categories: List<CategoryDto>
)

@Serializable
data class CategoryDto(
    val title: String,
    val url: String
)

@Serializable
data class ItemDto(
    val title: String,
    val url: String,
    val category: String
)