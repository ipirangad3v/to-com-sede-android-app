package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Item

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardItem(item: Item, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                navController.navigate(
                    "item_details" +
                    "/${item.id}"
                )
            },
        border = BorderStroke(
            1.dp, MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Card(
                border = BorderStroke(
                    1.dp, MaterialTheme.colorScheme.secondary,
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                GlideImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(text = item.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = item.price.toString().toCurrency(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}