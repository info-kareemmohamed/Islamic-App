package com.example.myapplication.islamic_tube.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val title: String,
    val url: String
)

@Serializable
data class SubCategoryDto(
    val name: String,
    val videos: List<VideoDto>
)

@Serializable
data class CategoryDto(
    val name: String,
    val subCategories: List<SubCategoryDto>
)

