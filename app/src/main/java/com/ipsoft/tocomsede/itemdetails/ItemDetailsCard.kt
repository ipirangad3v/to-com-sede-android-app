package com.ipsoft.tocomsede.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.ui.components.PriceTag
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.largePadding
import com.ipsoft.tocomsede.core.ui.theme.smallPadding

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemDetailsCard(itemDetailScreenState: ItemDetailScreenState) {
    itemDetailScreenState.item?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, smallPadding, 0.dp, 0.dp)
        ) {
            GlideImage(
                model = it.imageUrl,
                contentDescription = it.description,
                Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(largePadding)
                        .fillMaxWidth()
                ) {
                    Text(text = it.description)
                    Spacer(modifier = Modifier.padding(smallPadding))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriceTag(price = it.price)
                        ElevatedButton(
                            onClick = {},
                            colors = ButtonDefaults.elevatedButtonColors(
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
                                modifier = Modifier
                                    .padding(smallPadding)
                                    .wrapContentSize(Alignment.Center),
                                maxLines = 1,
                                color = Color.White
                            )
                        }
                        if (itemDetailScreenState.quantityInCart > 0) {
                            ElevatedButton(onClick = { }) {
                                Row(
                                    modifier = Modifier
                                        .padding(smallPadding)
                                        .wrapContentSize(Alignment.Center),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = itemDetailScreenState.quantityInCart.toString() + " no ",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(smallPadding),
                                        maxLines = 1,
                                        color = darkBlue80
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(smallPadding),
                                        tint = darkBlue80
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
