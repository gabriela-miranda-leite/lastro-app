package com.lastro.feature.transactions.ui.components

import com.lastro.core.domain.model.TransactionType
import com.lastro.core.ui.theme.LastroColors
import org.junit.Assert.assertEquals
import org.junit.Test

class AmountDisplayTest {

    @Test
    fun `INCOME maps to positive color`() {
        assertEquals(LastroColors.Positive, TransactionType.INCOME.toAmountColor())
    }

    @Test
    fun `EXPENSE maps to negative color`() {
        assertEquals(LastroColors.Negative, TransactionType.EXPENSE.toAmountColor())
    }

    @Test
    fun `all TransactionType values have a color mapping`() {
        TransactionType.entries.forEach { type ->
            val color = type.toAmountColor()
            assert(color != androidx.compose.ui.graphics.Color.Unspecified)
        }
    }
}
