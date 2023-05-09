package com.ipsoft.tocomsede.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.ui.theme.almostWhite
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80
import com.ipsoft.tocomsede.core.ui.theme.mediumPadding

@Composable
fun PriceTag(price: Double) {
    ElevatedCard(
        shape = RoundedCornerShape(mediumPadding),
        modifier = Modifier.padding(mediumPadding),
        colors = CardDefaults.elevatedCardColors(
            containerColor = darkBlue80,
            contentColor = almostWhite
        )
    ) {
        Text(
            text = price.toString().toCurrency(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(mediumPadding)
        )
    }
}
