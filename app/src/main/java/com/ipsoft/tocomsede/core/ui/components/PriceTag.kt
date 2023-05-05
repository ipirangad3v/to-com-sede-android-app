package com.ipsoft.tocomsede.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipsoft.tocomsede.core.extensions.toCurrency
import com.ipsoft.tocomsede.core.ui.theme.almostWhite
import com.ipsoft.tocomsede.core.ui.theme.darkBlue80

@Composable
fun PriceTag(price: Double) {
    ElevatedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = darkBlue80,
            contentColor = almostWhite
        )
    ) {
        Text(
            text = price.toString().toCurrency(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}