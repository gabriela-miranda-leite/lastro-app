package com.lastro.feature.transactions.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionItemTest {

    @Test
    fun `buildA11yLabel concatenates description, amount and date`() {
        val result = buildA11yLabel("Mercado", "R$ 50,00", "01/10/2026")
        assertEquals("Mercado, R$ 50,00, 01/10/2026", result)
    }

    @Test
    fun `buildA11yLabel handles empty description`() {
        val result = buildA11yLabel("", "R$ 10,00", "01/10/2026")
        assertEquals(", R$ 10,00, 01/10/2026", result)
    }
}
