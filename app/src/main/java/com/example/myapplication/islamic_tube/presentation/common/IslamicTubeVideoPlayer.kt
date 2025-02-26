package com.example.myapplication.islamic_tube.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.islamic_tube.presentation.util.extractYoutubeVideoId
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun IslamicTubeVideoPlayer(
    modifier: Modifier = Modifier,
    videoUrl: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isPlayerReady by remember { mutableStateOf(false) }
    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    // Create and configure the YouTubePlayerView
                    YouTubePlayerView(context).apply {
                        lifecycleOwner.lifecycle.addObserver(this)
                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(player: YouTubePlayer) {
                                youTubePlayer = player
                                videoUrl.extractYoutubeVideoId()?.let { id ->
                                    player.cueVideo(id, 0f)
                                }
                                isPlayerReady = true
                            }

                            override fun onError(
                                player: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                // Optional: handle error here
                                isPlayerReady = true
                            }
                        })
                    }
                },
                update = {
                    // Update the video when videoUrl changes and the player is ready
                    if (isPlayerReady) {
                        videoUrl.extractYoutubeVideoId()?.let { id ->
                            youTubePlayer?.cueVideo(id, 0f)
                        }
                    }
                }
            )
            if (!isPlayerReady) {
                // Show a loading indicator until the player is ready
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

