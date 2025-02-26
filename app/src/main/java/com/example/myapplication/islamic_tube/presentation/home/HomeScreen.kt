package com.example.myapplication.islamic_tube.presentation.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.core.domain.NetworkError
import com.example.myapplication.core.presentation.asUiText
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme
import com.example.myapplication.islamic_tube.domain.model.Section
import com.example.myapplication.islamic_tube.presentation.home.components.HomeHadithCard
import com.example.myapplication.islamic_tube.presentation.home.components.HomeScreenShimmerEffect
import com.example.myapplication.islamic_tube.presentation.home.components.HomeVideoCard
import com.example.myapplication.islamic_tube.presentation.home.components.TopHomeBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    notificationClick: () -> Unit,
    searchClick: () -> Unit,
    videoClick: (playList: String) -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    HomeScreen(
        isLoading = viewModel.isLoading.collectAsState().value,
        sections = viewModel.categories.collectAsState().value,
        errorMessage = viewModel.errorMessage.collectAsState(initial = null).value,
        notificationClick = notificationClick,
        searchClick = searchClick,
        videoClick = videoClick
    )
}


@Composable
private fun HomeScreen(
    sections: List<Section>,
    isLoading: Boolean,
    errorMessage: NetworkError?,
    notificationClick: () -> Unit,
    searchClick: () -> Unit,
    videoClick: (playList: String) -> Unit
) {

    val context = LocalContext.current


    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage.asUiText(context), Toast.LENGTH_LONG).show()
        }
    }


    TopHomeBar(
        notificationClick = notificationClick,
        searchClick = searchClick
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            if (isLoading) {
                itemsIndexed(items = (0..3).toList()) { rowIndex, _ ->
                    HomeScreenShimmerEffect(rowIndex)
                }
            } else {
                itemsIndexed(
                    items = sections,
                    key = { index, section -> section.name }
                ) { index, section ->

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.clickable { },
                                text = stringResource(R.string.home_screen_more),
                                color = colorResource(R.color.blue),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = section.name,
                                color = colorResource(R.color.black),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            contentPadding = PaddingValues(10.dp),
                            reverseLayout = true
                        ) {
                            items(section.categories, key = { it.name }) { category ->
                                HomeVideoCard(
                                    imageUrl = category.imageUrl,
                                    title = category.name,
                                    onClick = {
                                        videoClick(category.name)
                                    }
                                )
                            }
                        }

                        if (index == 0) {
                            //TODO: Add Hadith
                            HomeHadithCard(
                                hadithText = "إنَّ العَبْدَ لَيَتَكَلَّمُ بالكَلِمَةِ مِن رِضْوانِ اللَّهِ، لا يُلْقِي لها بالًا، يَرْفَعُهُ اللَّهُ بها دَرَجاتٍ، وإنَّ العَبْدَ لَيَتَكَلَّمُ بالكَلِمَةِ مِن سَخَطِ اللَّهِ، لا يُلْقِي لها بالًا، يَهْوِي بها في جَهَنَّمَ.",
                                narrator = "أبو هريرة",
                                source = "صحيح البخاري",
                                modifier = Modifier.padding(vertical = 5.dp)

                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(
            sections = emptyList(),
            isLoading = false,
            errorMessage = null,
            notificationClick = {},
            searchClick = {},
            videoClick = { _ -> }
        )
    }
}