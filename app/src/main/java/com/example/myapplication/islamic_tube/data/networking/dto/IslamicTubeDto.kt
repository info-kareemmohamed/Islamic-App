package com.example.myapplication.islamic_tube.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IslamicTubeDto(
    val sections: List<SectionDto>,
    @SerialName("total_video_count")
    val totalVideoCount: Int,
    @SerialName("playlist_count")
    val playlistCount: Int
)

@Serializable
data class SectionDto(
    val title: String,
    val categories: List<CategoryDto>
)

@Serializable
data class CategoryDto(
    @SerialName("playlist_id")
    val playlistId: String,
    val title: String,
    val url: String
)

@Serializable
data class ItemDto(
    val title: String,
    val url: String,
    val thumbnail: String,
    @SerialName("video_id")
    val videoId: String,
    @SerialName("playlist_id")
    val playlistId: String
)