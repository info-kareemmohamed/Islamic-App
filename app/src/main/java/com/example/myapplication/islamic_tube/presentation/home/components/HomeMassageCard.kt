package com.example.myapplication.islamic_tube.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R


@Composable
fun HomeHadithCard(
    hadithText: String,
    narrator: String,
    source: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()


    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2D4631),
                            Color(0xFF4B7350),
                            Color(0xFF233B25)

                        )
                    )
                )
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(id = R.string.home_screen_hadithText),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.width(5.dp))

                Icon(
                    painter = painterResource(id = R.drawable.islamic_icon),
                    contentDescription = "Search Icon",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )

            }

            Text(
                text = hadithText,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End

            )
            Text(
                narrator, color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                source, color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeHadithCardPreview() {
    HomeHadithCard(
        hadithText = "إنَّ العَبْدَ لَيَتَكَلَّمُ بالكَلِمَةِ مِن رِضْوانِ اللَّهِ، لا يُلْقِي لها بالًا، يَرْفَعُهُ اللَّهُ بها دَرَجاتٍ، وإنَّ العَبْدَ لَيَتَكَلَّمُ بالكَلِمَةِ مِن سَخَطِ اللَّهِ، لا يُلْقِي لها بالًا، يَهْوِي بها في جَهَنَّمَ.",
        narrator = "أبو هريرة",
        source = "صحيح البخاري"
    )
}