package com.lastro.core.common

import org.junit.Assert.assertEquals
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

    @Test
    fun `centsStringToReais converts valid string`() {
        assertEquals(java.math.BigDecimal("2.50"), "250".centsStringToReais())
    }

    @Test
    fun `centsStringToReais null returns zero`() {
        assertEquals(java.math.BigDecimal("0.00"), null.centsStringToReais())
    }

    @Test
    fun `centsStringToReais empty string returns zero`() {
        assertEquals(java.math.BigDecimal("0.00"), "".centsStringToReais())
    }

    @Test
    fun `centsStringToReais zero returns zero`() {
        assertEquals(java.math.BigDecimal("0.00"), "0".centsStringToReais())
    }

    @Test
    fun `centsStringToReais negative value divides correctly`() {
        assertEquals(java.math.BigDecimal("-1.00"), "-100".centsStringToReais())
    }

    @Test
    fun `centsStringToReais large value divides correctly`() {
        assertEquals(java.math.BigDecimal("1000.00"), "100000".centsStringToReais())
    }

    @Test
    fun `centsStringToReais non-numeric string returns zero`() {
        assertEquals(java.math.BigDecimal("0.00"), "abc".centsStringToReais())
    }
}
