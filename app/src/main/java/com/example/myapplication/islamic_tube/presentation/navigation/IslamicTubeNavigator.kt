package com.example.myapplication.islamic_tube.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.myapplication.core.presentation.navigation.Routes
import com.example.myapplication.islamic_tube.presentation.details.DetailsScreenRoot
import com.example.myapplication.islamic_tube.presentation.favorite.FavoriteScreenRoot
import com.example.myapplication.islamic_tube.presentation.home.HomeScreenRoot
import com.example.myapplication.islamic_tube.presentation.search.SearchScreenRoot


fun NavGraphBuilder.islamicTubeNavigatorGraph() {
    navigation<Routes.IslamicTubeNavigation>(
        startDestination = Routes.IslamicTubeNavigatorScreen
    ) {
        composable<Routes.IslamicTubeNavigatorScreen> {
            IslamicNavigator()
        }
    }
}


@Composable
private fun IslamicNavigator() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination


    // Find the current route for the bottom navigation
    val foundRoute by remember(currentDestination) {
        derivedStateOf {
            bottomRouteDataList.firstOrNull { item ->
                currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
            }
        }
    }

    // Determine if the bottom bar should be visible based on the current route
    val isBottomBarVisible by remember(currentDestination) {
        derivedStateOf {
            currentDestination?.hierarchy?.any { destination ->
                bottomRouteDataList.any { item ->
                    destination.hasRoute(item.route::class)
                }
            } == true
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible)
                IslamicTubeBottomNavigation(
                    selectedItemRoute = foundRoute?.route ?: Routes.HomeScreen,
                    onItemClick = { route -> navigateToTab(navController, route.route) }
                )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HomeScreen,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            composable<Routes.HomeScreen> {
                HomeScreenRoot(
                    notificationClick = {},
                    searchClick = { navController.navigate(Routes.SearchScreen) },
                    videoClick = { playlistName ->
                        navController.navigate(Routes.DetailsScreen(playlistName = playlistName))
                    }
                )
            }

            composable<Routes.DetailsScreen> {
                val playlistName = it.toRoute<Routes.DetailsScreen>().playlistName
                val isFromFavorite = it.toRoute<Routes.DetailsScreen>().isFromFavorite

                DetailsScreenRoot(
                    modifier = Modifier.padding(innerPadding),
                    playlistName = playlistName,
                    isFromFavorite = isFromFavorite
                )
            }

            composable<Routes.LatestScreen> {
                Box(modifier = Modifier.fillMaxSize())
            }

            composable<Routes.FavoriteScreen> {
                FavoriteScreenRoot { playlistName ->
                    navController.navigate(Routes.DetailsScreen(true, playlistName))
                }
            }

            composable<Routes.DownloadScreen> {
                Box(modifier = Modifier.fillMaxSize())
            }

            composable<Routes.SettingsScreen> {
                Box(modifier = Modifier.fillMaxSize())
            }

            composable<Routes.SearchScreen> {
                SearchScreenRoot(onClick = { playlistName ->
                    navController.navigate(Routes.DetailsScreen(playlistName = playlistName))
                })
            }

        }
    }
}


private fun navigateToTab(navController: NavController, route: Routes) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) { saveState = true }
        }
        restoreState = true
        launchSingleTop = true
    }
}