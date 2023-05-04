package com.ipsoft.tocomsede.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ipsoft.tocomsede.R

@Composable
fun TopProducts(topItems: ItemState) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = stringResource(R.string.top_products))
            Spacer(modifier = Modifier.padding(4.dp))
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                topItems.item.let { items ->
                    items(items.size) {
                        CardItem(item = items[it])
                    }
                }
            }
        }
    }
}