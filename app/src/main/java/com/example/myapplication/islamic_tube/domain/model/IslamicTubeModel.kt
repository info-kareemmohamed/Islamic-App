package com.example.myapplication.islamic_tube.domain.model

data class Video(
    val title: String,
    val url: String
)

data class SubCategory(
    val name: String,
    val videos: List<Video>
)

data class Category(
    val name: String,
    val subCategories: List<SubCategory>
)
