package com.example.myapplication.islamic_tube.presentation.search

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme
import com.example.myapplication.islamic_tube.domain.model.Category
import com.example.myapplication.islamic_tube.presentation.common.IslamicVideoCardListShimmerEffect
import com.example.myapplication.islamic_tube.presentation.common.VideoThumbnail
import com.example.myapplication.islamic_tube.presentation.search.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    onClick: (playlistName: String) -> Unit
) {
    SearchScreen(
        isLoading = viewModel.isLoading.collectAsState().value,
        searchQuery = viewModel.searchQuery.collectAsState().value,
        categoryList = viewModel.categoryList.collectAsState().value,
        onValueChange = viewModel::searchQueryChange,
        onSearch = viewModel::onSearch,
        onClick = onClick
    )
}

@Composable
private fun SearchScreen(
    isLoading: Boolean,
    searchQuery: String,
    categoryList: List<Category>,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClick: (playlistName: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding()
    ) {
        SearchBar(
            text = searchQuery,
            readOnly = false,
            onValueChange = onValueChange,
            onSearch = onSearch
        )
        Spacer(modifier = Modifier.height(25.dp))
        if (isLoading) {
            IslamicVideoCardListShimmerEffect()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(5.dp)
            ) {

                items(categoryList.size) { index ->

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            alignment = Alignment.End
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(categoryList[index].name) }
                    ) {
                        Text(
                            text = categoryList[index].name,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            color = colorResource(R.color.black),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        VideoThumbnail(
                            thumbnailUrl = categoryList[index].imageUrl,
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
}


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchBarPreview() {
    MyApplicationTheme {
        SearchScreen(
            isLoading = false,
            searchQuery = "",
            onValueChange = {},
            onSearch = {},
            categoryList = emptyList(),
            onClick = {}
        )
    }
}