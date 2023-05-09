package com.ipsoft.tocomsede.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.ui.components.PriceTag
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.defaultCartElevation
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemDetailsCard(itemDetailScreenState: ItemDetailScreenState) {
    itemDetailScreenState.item?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(mediumPadding)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize(),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultCartElevation)
            ) {
                GlideImage(
                    model = it.imageUrl,
                    contentDescription = it.description,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Spacer(modifier = Modifier.padding(mediumPadding))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultCartElevation)
            ) {
                Column(
                    modifier = Modifier
                        .padding(mediumPadding)
                        .fillMaxWidth()
                ) {
                    Text(text = it.description)
                    Spacer(modifier = Modifier.padding(mediumPadding))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriceTag(price = it.price)
                        SquaredButton(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = if (it.isAvailable) darkBlue80 else MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = if (it.isAvailable) {
                                    stringResource(id = R.string.quantity) + ": " + it.quantity.toString()
                                } else {
                                    stringResource(
                                        id = R.string.unavailable
                                    )
                                },
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(mediumPadding),
                                maxLines = 1,
                                color = Color.White
                            )
                        }
                        if (itemDetailScreenState.quantityInCart > 0) {
                            SquaredButton {
                                Text(
                                    text = stringResource(id = R.string.in_cart) + ": " + itemDetailScreenState.quantityInCart.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(mediumPadding),
                                    maxLines = 1,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
