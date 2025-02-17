package com.example.myapplication.islamic_tube.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.islamic_tube.presentation.util.extractYoutubeVideoId


@Composable
fun IslamicVideoCard(
    videoUrl: String,
    videoTitle: String,
    artistName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .width(160.dp)
            .clickable { onClick() }
    ) {
        val videoId = videoUrl.extractYoutubeVideoId()
        videoId?.let {
            val thumbnailUrl = "https://img.youtube.com/vi/$it/0.jpg"
            VideoThumbnail(
                thumbnailUrl = thumbnailUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
        Text(
            text = videoTitle,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            color = colorResource(R.color.black),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = artistName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            color = colorResource(R.color.slate_gray),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

