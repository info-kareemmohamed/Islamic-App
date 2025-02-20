package com.example.myapplication.islamic_tube.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VideoListConverter {

    @TypeConverter
    fun fromVideoList(videoEntities: List<VideoEntity>): String {
        return Json.encodeToString(videoEntities)
    }

    @TypeConverter
    fun toVideoList(json: String): List<VideoEntity> {
        return Json.decodeFromString(json)
    }
}