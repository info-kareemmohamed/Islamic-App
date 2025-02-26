package com.example.myapplication.islamic_tube.presentation.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.R
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.presentation.common.VideoThumbnail
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavoriteScreenRoot(
    viewModel: FavoriteViewModel = koinViewModel<FavoriteViewModel>(),
    onClick: (playlistName: String) -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    FavoriteScreen(categories = categories, onClick = onClick)
}


@Composable
fun FavoriteScreen(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onClick: (playlistName: String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(vertical = 5.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = stringResource(R.string.favorite_screen_title),
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(R.color.black)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(categories.size) { index ->

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(categories[index].name)
                        }
                ) {
                    Text(
                        text = categories[index].name,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = colorResource(R.color.black),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    VideoThumbnail(
                        thumbnailUrl = categories[index].imageUrl,
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


@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    MyApplicationTheme {
        FavoriteScreen(
            categories = emptyList()
        ) { _ -> }
    }
}
