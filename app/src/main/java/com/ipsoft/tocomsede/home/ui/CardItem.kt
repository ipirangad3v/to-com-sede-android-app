package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.core.model.Item

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardItem(item: Item, navController: NavHostController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(
                "item_details" +
                "/${item.id}"
            )
        }
        .fillMaxWidth()) {
        Column(
            horizontalAlignment = CenterHorizontally, modifier = Modifier
                .padding(2.dp, 2.dp, 2.dp, 8.dp)
        ) {
            Card {
                GlideImage(
                    model = item.imageUrl,
                    contentDescription = item.description,
                    contentScale = ContentScale.Inside
                )

            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = item.name, fontWeight = Bold)
        }
    }
}