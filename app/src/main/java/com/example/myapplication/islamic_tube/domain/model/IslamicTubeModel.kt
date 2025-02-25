package com.example.myapplication.islamic_tube.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val title: String,
    val url: String,
)

@Serializable
data class SubCategory(
    val name: String,
    val videos: List<Video>,
    val imageUrl: String
)

@Serializable
data class Category(
    val name: String,
    val subCategories: List<SubCategory>
)
