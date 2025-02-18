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
    data object DetailsScreen : Routes

    @Serializable
    data object LatestScreen : Routes

    @Serializable
    data object IslamicTubeNavigation : Routes

    @Serializable
    data object IslamicTubeNavigatorScreen : Routes
}