package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.ui.components.PriceTag
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.softBlue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardListItem(item: Item, navController: NavHostController) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                navController.navigate(
                    "item_details" +
                    "/${item.id}"
                )
            },
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.elevatedCardColors(
            containerColor = softBlue,
        )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            ElevatedCard(
                shape = MaterialTheme.shapes.small
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
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = item.name, style = MaterialTheme.typography.headlineSmall, maxLines = 1)
                Spacer(modifier = Modifier.weight(1f))
                SquaredButton(text = item.vendor, modifier = Modifier.offset((-210).dp, (65).dp))
                PriceTag(price = item.price)
            }
        }
    }
}
