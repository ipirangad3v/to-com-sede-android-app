package com.ipsoft.tocomsede.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartItemList(itemState: CartViewModel.CartItemState) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            Spacer(modifier = Modifier.padding(4.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                itemState.items.let { items ->
                    items(items.size) {
                        Spacer(modifier = Modifier.padding(8.dp))
                        CartCardListItem(item = items[it])
                    }
                }
            }
        }
    }
}
