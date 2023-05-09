package com.ipsoft.tocomsede.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ipsoft.tocomsede.R
import com.ipsoft.tocomsede.core.ui.components.SquaredButton
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding
import com.ipsoft.tocomsede.core.ui.theme.smallPadding

@Composable
fun QuantitySelector(selectedQuantity: MutableState<Int>, maxQuantity: Int, available: Boolean) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.select_quantity))
        Spacer(modifier = Modifier.padding(smallPadding))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SquaredButton(
                onClick = { if (available && selectedQuantity.value > 1) selectedQuantity.value-- }
            ) {
                Text(
                    text = "-",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(mediumPadding),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.padding(mediumPadding))
            Text(text = selectedQuantity.value.toString())
            Spacer(modifier = Modifier.padding(mediumPadding))
            SquaredButton(
                onClick = { if (available && selectedQuantity.value < maxQuantity) selectedQuantity.value++ }
            ) {
                Text(
                    text = "+",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(mediumPadding),
                    maxLines = 1
                )
            }
        }
    }
}
