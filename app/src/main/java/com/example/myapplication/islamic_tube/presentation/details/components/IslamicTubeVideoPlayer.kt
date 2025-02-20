package com.example.myapplication.islamic_tube.presentation.details.components

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
fun IslamicTubeVideoPlayer(modifier: Modifier = Modifier, videoUrl: String) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isPlayerReady by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    // Create and configure the YouTubePlayerView
                    val youTubePlayerView = YouTubePlayerView(ctx).apply {
                        // Attach the lifecycle observer to manage lifecycle events automatically
                        lifecycleOwner.lifecycle.addObserver(this)
                        // Add a listener to load the YouTube video when the player is ready
                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                                videoUrl.extractYoutubeVideoId()?.let { videoId ->
                                    youTubePlayer.cueVideo(videoId, 0f)
                                }
                                isPlayerReady = true
                            }

                            override fun onError(
                                youTubePlayer: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                super.onError(youTubePlayer, error)
                                //TODO Handle error
                                isPlayerReady = true
                            }
                        }
                        )
                    }
                    youTubePlayerView
                }
            )

            // Show a loading indicator until the video player is ready
            if (!isPlayerReady) {
                Box(
                    modifier = Modifier
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
