package com.example.myapplication.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.core.presentation.navigation.NavGraph
import com.example.myapplication.core.presentation.navigation.Routes
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NavGraph(startDestination = Routes.IslamicTubeNavigation)
            }
        }
    }
}

