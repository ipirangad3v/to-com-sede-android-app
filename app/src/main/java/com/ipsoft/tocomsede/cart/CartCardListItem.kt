package com.ipsoft.tocomsede.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.ui.theme.softBlue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartCardListItem(item: Pair<Item, Int>) {
    Column(modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp)) {
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
                        model = item.first.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.first.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material.MaterialTheme.colors.onSurface
                )
                Text(
                    text = "·",
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material.MaterialTheme.colors.onSurface
                )
                Text(
                    text = item.first.price.toString().toCurrency(),
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material.MaterialTheme.colors.onSurface
                )
                Text(
                    text = "X ${item.second}",
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material.MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

