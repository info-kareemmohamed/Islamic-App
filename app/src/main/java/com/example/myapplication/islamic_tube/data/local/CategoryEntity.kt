package com.example.myapplication.islamic_tube.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


data class CategoryNameAndFirstVideoUrl(
    val name: String,
    val firstVideo: VideoEntity
)

@Serializable
data class VideoEntity(
    val title: String,
    val url: String,
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val name: String,
    val videoEntities: List<VideoEntity>
)