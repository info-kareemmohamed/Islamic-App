package com.example.myapplication.islamic_tube.presentation.util

fun String.extractYoutubeVideoId(): String? {
    val regex1 = "(?<=v=)[^&#]+".toRegex()
    val regex2 = "(?<=youtu\\.be/)[^?&#]+".toRegex()
    return regex1.find(this)?.value ?: regex2.find(this)?.value
}