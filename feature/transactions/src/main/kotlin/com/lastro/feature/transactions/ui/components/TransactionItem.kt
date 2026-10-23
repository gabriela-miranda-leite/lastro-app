package com.lastro.feature.transactions.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.lastro.core.common.toBRL
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete(transaction.id)
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(end = LastroSpacing.xl),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir transação",
                    tint = MaterialTheme.colorScheme.onError,
                )
            }
        },
        modifier = modifier,
        enableDismissFromStartToEnd = false,
    ) {
        TransactionItemContent(transaction = transaction)
    }
}

@Composable
private fun TransactionItemContent(transaction: Transaction) {
    val amountLabel = if (transaction.direction == TransactionType.INCOME) {
        "+${transaction.amount.toBRL()}"
    } else {
        "-${transaction.amount.toBRL()}"
    }
    val dateLabel = transaction.date.format(dateFormatter)
    val descriptionLabel = transaction.description ?: transaction.category?.name ?: "Transação"
    val a11yLabel = buildA11yLabel(descriptionLabel, amountLabel, dateLabel)

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = a11yLabel },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = LastroSpacing.xl, vertical = LastroSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CategoryIcon(transaction)
            Spacer(modifier = Modifier.width(LastroSpacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = descriptionLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.width(LastroSpacing.md))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = amountLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = transaction.direction.toAmountColor(),
                )
                SyncStatusIndicator(
                    syncStatus = transaction.syncStatus,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun CategoryIcon(transaction: Transaction) {
    val emoji = transaction.category?.icon
    if (emoji != null) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.titleMedium,
        )
    } else {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )
    }
}

fun buildA11yLabel(description: String, amount: String, date: String): String =
    "$description, $amount, $date"
