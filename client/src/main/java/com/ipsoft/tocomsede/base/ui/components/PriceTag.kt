package com.ipsoft.tocomsede.base.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ipsoft.tocomsede.base.ui.theme.almostWhite
import com.ipsoft.tocomsede.base.ui.theme.darkBlue80
import com.ipsoft.tocomsede.base.ui.theme.mediumPadding
import com.ipsoft.tocomsede.base.ui.theme.smallPadding
import com.ipsoft.tocomsede.core.extensions.toCurrency

@Composable
fun PriceTag(price: Double) {
    ElevatedButton(
        onClick = {},
        modifier = Modifier.padding(mediumPadding),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = darkBlue80,
            contentColor = almostWhite
        )
    ) {
        Text(
            text = price.toString().toCurrency(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(smallPadding)
        )
    }
}
