package com.example.myapplication.islamic_tube.presentation.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R
import com.example.myapplication.core.presentation.theme.ui.MyApplicationTheme

@Composable
fun CategoryListDialog(
    items: List<String>,
    initialSelectedItems: Set<String>,
    onDismissRequest: () -> Unit,
    onSelectedItems: (selectedItems: List<String>) -> Unit,
    onCreateNewCategory: () -> Unit
) {
    var selectedItems by remember { mutableStateOf(initialSelectedItems) }

    LaunchedEffect(initialSelectedItems) { selectedItems = initialSelectedItems }


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white),
            ),
            border = BorderStroke(1.dp, colorResource(id = R.color.slate_gray)),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.details_screen_save_video_in),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(id = R.color.olive_green),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                if (items.isEmpty()) {
                    Text(
                        text = stringResource(R.string.details_screen_create_new_list_message),
                        color = colorResource(id = R.color.slate_gray),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(items) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = colorResource(id = R.color.black)
                                )
                                Checkbox(
                                    checked = selectedItems.contains(item),
                                    onCheckedChange = { isChecked ->
                                        selectedItems = if (isChecked) {
                                            selectedItems + item
                                        } else {
                                            selectedItems - item
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onCreateNewCategory() },

                        ) {
                        Text(
                            text = stringResource(R.string.details_screen_create_new_list),
                            color = colorResource(R.color.white),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }
                    Button(
                        onClick = {
                            onSelectedItems(selectedItems.toList())
                            onDismissRequest()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.details_screen_add),
                            color = colorResource(R.color.white),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }
                }
            }
        }
    }
}


@Preview
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryListDialogPreview() {
    MyApplicationTheme {
        CategoryListDialog(
            items = listOf("الخيار 1", "الخيار 2", "الخيار 3", "الخيار 4"),
            initialSelectedItems = setOf("الخيار 1", "الخيار 3"),
            onDismissRequest = {},
            onCreateNewCategory = {},
            onSelectedItems = {}
        )
    }
}