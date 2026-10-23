package com.lastro.feature.transactions.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.lastro.core.common.toBRL
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.ui.theme.LastroColors
import java.math.BigDecimal

@Composable
fun AmountDisplay(
    amount: BigDecimal,
    direction: TransactionType,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.displayLarge,
) {
    val color = direction.toAmountColor()
    val prefix = if (direction == TransactionType.INCOME) "+" else "-"

    Text(
        text = "$prefix${amount.toBRL()}",
        style = style,
        color = color,
        modifier = modifier,
    )
}

fun TransactionType.toAmountColor(): Color = when (this) {
    TransactionType.INCOME -> LastroColors.Positive
    TransactionType.EXPENSE -> LastroColors.Negative
}
