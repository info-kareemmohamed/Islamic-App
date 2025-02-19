package com.example.myapplication.islamic_tube.presentation.navigation

import com.example.myapplication.R
import com.example.myapplication.core.presentation.navigation.Routes

data class BottomRouteData(
    val route: Routes,
    val title: Int,
    val icon: Int,
)


val bottomRouteDataList = listOf(
    BottomRouteData(Routes.SettingsScreen, R.string.bottom_navigation_settings, R.drawable.ic_settings),
    BottomRouteData(Routes.LatestScreen, R.string.bottom_navigation_download, R.drawable.ic_download),
    BottomRouteData(Routes.FavoriteScreen, R.string.bottom_navigation_favorite, R.drawable.ic_favorite),
    BottomRouteData(Routes.LatestScreen, R.string.bottom_navigation_latest, R.drawable.ic_latest),
    BottomRouteData(Routes.HomeScreen, R.string.bottom_navigation_home, R.drawable.ic_home)
)

