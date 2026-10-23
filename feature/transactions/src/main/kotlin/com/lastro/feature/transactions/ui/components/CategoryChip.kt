package com.lastro.feature.transactions.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lastro.core.domain.model.Category

@Composable
fun CategoryChip(
    category: Category,
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelected(!selected) },
        label = { Text(category.name) },
        leadingIcon = {
            if (category.icon != null) {
                Text(text = category.icon, modifier = Modifier.size(18.dp))
            } else {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        },
        modifier = modifier,
    )
}
