package com.example.myapplication.islamic_tube.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.presentation.util.extractYoutubeVideoId

@Composable
fun IslamicListVideos(
    relatedVideos: List<Video>,
    modifier: Modifier = Modifier,
    onClick: (selectedVideo: Video) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(5.dp)
    ) {

        items(relatedVideos.size) { index ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onClick(relatedVideos[index]) }
            ) {
                Text(
                    text = relatedVideos[index].title,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    color = colorResource(R.color.black),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val videoId by remember { mutableStateOf(relatedVideos[index].url.extractYoutubeVideoId()) }
                videoId?.let {
                    val thumbnailUrl = "https://img.youtube.com/vi/$it/0.jpg"
                    VideoThumbnail(
                        thumbnailUrl = thumbnailUrl,
                        modifier = Modifier
                            .width(170.dp)
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }
        }
    }
}