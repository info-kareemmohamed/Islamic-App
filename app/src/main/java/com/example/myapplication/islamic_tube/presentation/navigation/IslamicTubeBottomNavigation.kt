package com.example.myapplication.islamic_tube.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.presentation.navigation.Routes
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme

@Composable
fun IslamicTubeBottomNavigation(
    items: List<BottomRouteData> = bottomRouteDataList,
    selectedItemRoute: Routes,
    onItemClick: (BottomRouteData) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        tonalElevation = 10.dp
    ) {
        items.forEach { item: BottomRouteData ->
            NavigationBarItem(
                selected = selectedItemRoute == item.route,
                onClick = {
                    onItemClick(item)
                },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(id = item.title),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsBottomNavigationPreview() {
    MyApplicationTheme(dynamicColor = false) {
        IslamicTubeBottomNavigation(items = bottomRouteDataList,
            selectedItemRoute = Routes.HomeScreen,
            onItemClick = {})
    }
}