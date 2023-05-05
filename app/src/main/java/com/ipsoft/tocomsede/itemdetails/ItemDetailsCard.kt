package com.ipsoft.tocomsede.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.ui.components.PriceTag
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.softBlue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemDetailsCard(itemState: ItemState) {
    itemState.item?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize(),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = softBlue,
                )
            ) {
                GlideImage(
                    model = it.imageUrl,
                    contentDescription = it.description,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = softBlue,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = it.name,
                        fontSize = 20.sp,
                        fontWeight = Bold,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(alignment = CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = it.description)
                    Spacer(modifier = Modifier.padding(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriceTag(price = it.price)
                        SquaredButton(text = stringResource(id = R.string.quantity) + ": " + it.quantity.toString())
                    }
                }
            }
        }
    }
}