package com.example.myapplication.islamic_tube.domain.model


data class Section(
    val name: String,
    val categories: List<Category>
)

data class Category(
    val name: String,
    val imageUrl: String,
)


data class Playlist(
    val name: String,
    val videos: List<Video>
)

data class Video(
    val title: String,
    val url: String,
)
