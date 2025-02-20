package com.example.myapplication.islamic_tube.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme
import com.example.myapplication.islamic_tube.presentation.common.shimmerEffect

@Composable
fun HomeVideoCardShimmerEffect() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(160.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .shimmerEffect()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun IslamicVideoCardShimmerEffectPreview() {
    MyApplicationTheme {
        HomeVideoCardShimmerEffect()
    }
}
