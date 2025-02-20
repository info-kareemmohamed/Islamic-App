package com.example.myapplication.islamic_tube.presentation.details

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.core.presentation.asUiText
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme
import com.example.myapplication.islamic_tube.domain.model.Video
import com.example.myapplication.islamic_tube.presentation.common.IslamicListVideos
import com.example.myapplication.islamic_tube.presentation.common.IslamicTubeVideoPlayer
import com.example.myapplication.islamic_tube.presentation.common.IslamicVideoCardListShimmerEffect
import com.example.myapplication.islamic_tube.presentation.details.components.CategoryListDialog
import com.example.myapplication.islamic_tube.presentation.details.components.CreateCategoryDialog
import com.example.myapplication.islamic_tube.presentation.details.mvi.DetailsIntent
import com.example.myapplication.islamic_tube.presentation.details.mvi.DetailsState
import com.example.myapplication.islamic_tube.presentation.details.mvi.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = koinViewModel(),
    selectedVideo: Video,
    categoryName: String,
    subCategoryName: String,
    onVideoClick: (video: Video, category: String, subCategory: String) -> Unit,
) {
    val context = LocalContext.current

    /*
  * To determine the data source (local storage or network), I will check the `categoryName`:
  * - If `categoryName` is **not empty**, it means the request is coming from the **home screen**,
  *   so I will fetch data from the **network**.
  * - If `categoryName` is **empty**, it means the request is coming from the **favorites screen**,
  *   so I will fetch data from the **local storage**.
  */
    if (categoryName.isNotEmpty()) {
        viewModel.onIntent(
            DetailsIntent.LoadDataFromNetwork(selectedVideo, categoryName, subCategoryName)
        )
    } else {
        viewModel.onIntent(DetailsIntent.LoadDataFromLocal(selectedVideo, subCategoryName))
    }

    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage?.asUiText(context), Toast.LENGTH_LONG).show()
        }
    }

    val state = viewModel.state.collectAsState().value
    DetailsScreen(modifier, state, viewModel::onIntent) { newVideo, currentCategory ->
        if (newVideo != selectedVideo) onVideoClick(newVideo, categoryName, currentCategory)
    }
}

@Composable
private fun DetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailsState,
    onEvent: (DetailsIntent) -> Unit,
    onVideoClick: (video: Video, category: String) -> Unit,
) {
    val context = LocalContext.current

    if (state.isCategoryListDialogVisible) {
        CategoryListDialog(
            items = state.savedCategoryNames,
            initialSelectedItems = state.videoCategoryNames.toSet(),
            onDismissRequest = { onEvent(DetailsIntent.ToggleCategoryListDialog(false)) },
            onSelectedItems = { items ->
                onEvent(
                    DetailsIntent.SaveVideo(
                        state.currentVideo,
                        items
                    )
                )
            },
            onCreateNewCategory = { onEvent(DetailsIntent.ToggleCreateCategoryDialog(true)) }
        )
    }

    if (state.isCreateCategoryDialogVisible) {
        CreateCategoryDialog(
            onDismissRequest = { onEvent(DetailsIntent.ToggleCreateCategoryDialog(false)) },
            onConfirm = { onEvent(DetailsIntent.CreateCategory(it)) }
        )
    }

    Column(modifier.fillMaxSize()) {
        IslamicTubeVideoPlayer(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            videoUrl = state.currentVideo.url
        )
        Text(
            text = state.currentVideo.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onEvent(DetailsIntent.ToggleCategoryListDialog(!state.isCategoryListDialogVisible))
                        },
                    painter = painterResource(
                        id = if (state.videoCategoryNames.isEmpty()) R.drawable.ic_save_false
                        else R.drawable.ic_save_true
                    ),
                    contentDescription = "",
                    tint = colorResource(R.color.slate_gray)
                )
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, state.currentVideo.url)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        },
                    imageVector = Icons.Default.Share,
                    contentDescription = "",
                    tint = colorResource(R.color.slate_gray)
                )
            }
            Text(
                text = state.currentCategory,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.slate_gray)
            )
        }
        Text(
            text = stringResource(R.string.details_screen_more_videos),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            textDecoration = TextDecoration.Underline
        )
        if (state.isLoading) {
            IslamicVideoCardListShimmerEffect()
        } else {
            IslamicListVideos(relatedVideos = state.relatedVideos) { video ->
                onVideoClick(video, state.currentCategory)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    MyApplicationTheme {
        DetailsScreen(
            state = DetailsState(
                relatedVideos = listOf(
                    Video(
                        title = "تثبيت وتربيط و تدبر سورة الزلزلة",
                        url = "https://www.youtube.com/watch?v=AsFhMZE5o1U&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=22&pp=iAQB"
                    ),
                    Video(
                        title = "تثبيت وتربيط و تدبر سورة العاديات",
                        url = "https://www.youtube.com/watch?v=zLStmjf3Xww&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=23&pp=iAQB"
                    ),
                    Video(
                        title = "تثبيت وتربيط و تدبر سورة القارعة",
                        url = "https://www.youtube.com/watch?v=E12ENYhmd2s&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=24&pp=iAQB"

                    ),
                    Video(
                        title = "تثبيت وتربيط و تدبر سورة الزلزلة",
                        url = "https://www.youtube.com/watch?v=AsFhMZE5o1U&list=PL0146TQmugPBpIPL1OLqf6zQ3qeVrWCnY&index=22&pp=iAQB"
                    ),
                )
            ),
            onEvent = { },
        ) { _, _ ->

        }

    }
}
