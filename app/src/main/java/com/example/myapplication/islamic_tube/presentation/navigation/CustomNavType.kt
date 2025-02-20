package com.example.myapplication.islamic_tube.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.myapplication.islamic_tube.domain.model.Video
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val VideoType = object : NavType<Video>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Video? {
            return bundle.getString(key)?.let { Json.decodeFromString(it) }
        }

        override fun parseValue(value: String): Video {
            return Json.decodeFromString(Uri.decode(value))
        }


        override fun put(bundle: Bundle, key: String, value: Video) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: Video): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}
