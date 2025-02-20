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

    @TypeConverter
    fun fromVideoEntityJson(json: String?): VideoEntity? {
        return json?.let { Json.decodeFromString<VideoEntity>(it) }
    }

    @TypeConverter
    fun videoEntityToJson(video: VideoEntity?): String? {
        return video?.let { Json.encodeToString(it) }
    }
}