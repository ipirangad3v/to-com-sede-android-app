package com.ipsoft.tocomsede.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.model.Item
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.softBlue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartCardListItem(item: Pair<Item, Int>, onItemDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp),
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
                        model = item.first.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.first.name,
            style = MaterialTheme.typography.bodySmall,
            color = androidx.compose.material.MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.first.price.toString().toCurrency(),
            style = MaterialTheme.typography.titleSmall,
            color = androidx.compose.material.MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.selected_quantity) + ": ${item.second}",
            style = MaterialTheme.typography.titleSmall,
            color = androidx.compose.material.MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        RemoveButton(onItemDeleteClick = onItemDeleteClick)
    }
}

@Composable
fun RemoveButton(onItemDeleteClick: () -> Unit) {
    SquaredButton(
        modifier = Modifier
            .wrapContentSize(),
        onClick = { onItemDeleteClick() },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = androidx.compose.material.MaterialTheme.colors.onSurface
        )
    }
}
