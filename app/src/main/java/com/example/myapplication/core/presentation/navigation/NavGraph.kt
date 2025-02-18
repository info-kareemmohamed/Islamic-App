package com.example.myapplication.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.myapplication.islamic_tube.presentation.home.HomeScreenRoot


@Composable
fun NavGraph(startDestination: Routes) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation<Routes.IslamicTubeNavigation>(
            startDestination = Routes.HomeScreen
        ) {
            composable<Routes.HomeScreen> {
                HomeScreenRoot(
                    notificationClick = {},
                    searchClick = {}
                )
            }
        }
    }
}