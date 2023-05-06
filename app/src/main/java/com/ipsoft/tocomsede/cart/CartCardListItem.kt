package com.ipsoft.tocomsede.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.ui.components.PriceTag
import com.ipsoft.tocomsede.core.ui.theme.softBlue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartCardListItem(item: Pair<Item, Int>) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.elevatedCardColors(
            containerColor = softBlue
        )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            ElevatedCard(
                shape = MaterialTheme.shapes.small
            ) {
                GlideImage(
                    model = item.first.imageUrl,
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
                Text(
                    text = item.first.name,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriceTag(price = item.first.price)
                    Text(
                        text = "X${item.second}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
