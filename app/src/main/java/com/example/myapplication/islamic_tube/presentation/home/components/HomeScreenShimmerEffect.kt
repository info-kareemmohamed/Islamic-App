package com.example.myapplication.islamic_tube.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.islamic_tube.presentation.common.shimmerEffect

@Composable
fun HomeScreenShimmerEffect(rowIndex: Int) {
    if (rowIndex == 1) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .shimmerEffect()
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(10.dp),
        reverseLayout = true
    ) {
        items(10) {
            HomeVideoCardShimmerEffect()
        }
    }
}