package com.lastro.feature.transactions.ui.components

import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.ui.theme.LastroColors
import org.junit.Assert.assertEquals
import org.junit.Test

class SyncStatusIndicatorTest {

    @Test
    fun `SYNCED maps to green color and check icon`() {
        val state = SyncStatus.SYNCED.toIndicatorState()
        assertEquals(LastroColors.Positive, state.tint)
        assertEquals("Sincronizado", state.contentDescription)
    }

    @Test
    fun `PENDING maps to orange color and refresh icon`() {
        val state = SyncStatus.PENDING.toIndicatorState()
        assertEquals(androidx.compose.ui.graphics.Color(0xFFFF9800), state.tint)
        assertEquals("Sincronização pendente", state.contentDescription)
    }

    @Test
    fun `ERROR maps to red color and warning icon`() {
        val state = SyncStatus.ERROR.toIndicatorState()
        assertEquals(LastroColors.Negative, state.tint)
        assertEquals("Erro de sincronização", state.contentDescription)
    }

    @Test
    fun `all SyncStatus values have a mapping`() {
        SyncStatus.entries.forEach { status ->
            val state = status.toIndicatorState()
            assertTrue(state.contentDescription.isNotBlank())
        }
    }

    private fun assertTrue(value: Boolean) = org.junit.Assert.assertTrue(value)
}
