package com.ipsoft.tocomsede.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ipsoft.tocomsede.core.ui.state.CartItemState

@Composable
fun CartItemList(
    itemState: CartItemState,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {
            val height =
                if (itemState.items.size >= 2) ((itemState.items.size * 320) / 2).dp else ((itemState.items.size * 320)).dp
            Spacer(modifier = Modifier.padding(4.dp))
            LazyVerticalGrid(
                modifier = Modifier.height(height),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemState.items.let { items ->
                    items(items.size) {
                        Spacer(modifier = Modifier.padding(8.dp))
                        CartCardListItem(item = items[it]) {
                            cartViewModel.removeItem(items[it])
                        }
                    }
                }
            }
        }
    }
}
