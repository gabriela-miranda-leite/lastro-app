package com.lastro.core.common

import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class CurrencyFormatterTest {

    @Test
    fun `toBRL formats BigDecimal with BRL symbol`() {
        val result = BigDecimal("100.00").toBRL()
        assertTrue("expected R$ symbol, got: $result", result.contains("R$") || result.contains("R\$"))
    }

    @Test
    fun `toBRL formats integer value correctly`() {
        val result = BigDecimal("1000").toBRL()
        assertTrue(result.contains("1.000") || result.contains("1,000"))
    }

    @Test
    fun `toBRL formats decimal value correctly`() {
        val result = BigDecimal("49.99").toBRL()
        assertTrue(result.contains("49") && (result.contains(",99") || result.contains(".99")))
    }

    @Test
    fun `centsToBRL divides by 100`() {
        val result = 10000L.centsToBRL()
        assertTrue(result.contains("100"))
    }

    @Test
    fun `Double toBRL formats number`() {
        val result = 50.0.toBRL()
        assertTrue(result.contains("50"))
    }
}
