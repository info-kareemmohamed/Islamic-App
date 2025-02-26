package com.example.myapplication.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object HomeScreen : Routes

    @Serializable
    data object SearchScreen : Routes

    @Serializable
    data object FavoriteScreen : Routes

    @Serializable
    data object SettingsScreen : Routes

    @Serializable
    data class DetailsScreen(val isFromFavorite: Boolean = false, val playlistName: String) : Routes

    @Serializable
    data object LatestScreen : Routes

    @Serializable
    data object DownloadScreen : Routes

    @Serializable
    data object IslamicTubeNavigation : Routes

    @Serializable
    data object IslamicTubeNavigatorScreen : Routes
}