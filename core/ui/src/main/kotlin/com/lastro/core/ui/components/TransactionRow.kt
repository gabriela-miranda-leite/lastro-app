package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroTransactionRow(
    name: String,
    meta: String,
    amount: String,
    amountColor: Color,
    iconPainter: Painter,
    iconVariant: IconWrapVariant,
    modifier: Modifier = Modifier,
    a11yLabel: String = "$name, $amount, $meta",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = LastroSpacing.minTouchTarget)
            .padding(vertical = LastroSpacing.listItemVertical)
            .semantics { contentDescription = a11yLabel },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LastroIconWrap(
            painter = iconPainter,
            variant = iconVariant,
            wrapSize = LastroIconWrapSize.sm,
            iconSize = LastroIconSize.sm,
        )
        Spacer(modifier = Modifier.width(LastroSpacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = meta,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium,
            color = amountColor,
        )
    }
}

@Preview(name = "TransactionRow despesa - Dark")
@Composable
private fun PreviewExpense() = LastroTheme {
    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(LastroSpacing.xl)) {
        val painter = androidx.compose.ui.res.painterResource(com.lastro.core.ui.icon.LastroIcons.Home)
        LastroTransactionRow(
            name = "Aluguel",
            meta = "Moradia · 5 nov",
            amount = "−R$ 1.800",
            amountColor = LastroColors.Negative,
            iconPainter = painter,
            iconVariant = IconWrapVariant.Negative,
        )
    }
}

@Preview(name = "TransactionRow receita - Light")
@Composable
private fun PreviewIncome() = LastroTheme(darkTheme = false) {
    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(LastroSpacing.xl)) {
        val painter = androidx.compose.ui.res.painterResource(com.lastro.core.ui.icon.LastroIcons.TrendingUp)
        LastroTransactionRow(
            name = "Salário",
            meta = "Receita · 1 nov",
            amount = "+R$ 6.500",
            amountColor = LastroColors.Positive,
            iconPainter = painter,
            iconVariant = IconWrapVariant.Positive,
        )
    }
}
