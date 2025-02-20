package com.example.myapplication.islamic_tube.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IslamicVideoCardListShimmerEffect(count: Int = 5) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(count) {
            IslamicVideoCardShimmerEffect()
        }
    }
}

@Preview
@Composable
fun DetailsVideoCardShimmerEffectPreview() {
    IslamicVideoCardListShimmerEffect()
}