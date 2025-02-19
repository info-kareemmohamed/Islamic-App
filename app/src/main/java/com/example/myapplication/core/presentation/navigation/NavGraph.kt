package com.example.myapplication.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.islamic_tube.presentation.navigation.islamicTubeNavigatorGraph


@Composable
fun NavGraph(startDestination: Routes) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        islamicTubeNavigatorGraph()
    }
}