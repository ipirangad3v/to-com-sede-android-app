package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme.colors
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
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.ui.theme.softBlue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CardListItem(item: Item, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(0.dp, 8.dp, 0.dp, 8.dp)
            .clickable {
                navController.navigate(
                    "item_details" +
                        "/${item.id}"
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .wrapContentSize(),
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.elevatedCardColors(
                containerColor = softBlue
            ),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            Column(modifier = Modifier.wrapContentSize()) {
                ElevatedCard(
                    shape = MaterialTheme.shapes.small
                ) {
                    GlideImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurface
        )
        Text(
            text = item.price.toString().toCurrency(),
            style = MaterialTheme.typography.titleSmall,
            color = colors.onSurface
        )
    }
}
