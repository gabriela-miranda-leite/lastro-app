package com.lastro.core.ui

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyVisualTransformationTest {

    private val transformation = com.lastro.core.ui.components.moneyVisualTransformationForTest

    @Test
    fun `zero digits renders R$ 0,00`() {
        val result = transformation.filter(AnnotatedString("0"))
        assertEquals("R$ 0,00", result.text.text)
    }

    @Test
    fun `digits 250 renders R$ 2,50`() {
        val result = transformation.filter(AnnotatedString("250"))
        assertEquals("R$ 2,50", result.text.text)
    }

    @Test
    fun `digits 25000 renders R$ 250,00`() {
        val result = transformation.filter(AnnotatedString("25000"))
        assertEquals("R$ 250,00", result.text.text)
    }

    @Test
    fun `digits 1000000 renders R$ 10.000,00`() {
        val result = transformation.filter(AnnotatedString("1000000"))
        assertEquals("R$ 10.000,00", result.text.text)
    }

    @Test
    fun `non-digit characters are ignored`() {
        val result = transformation.filter(AnnotatedString("abc250xyz"))
        assertEquals("R$ 2,50", result.text.text)
    }

    @Test
    fun `empty string renders R$ 0,00`() {
        val result = transformation.filter(AnnotatedString(""))
        assertEquals("R$ 0,00", result.text.text)
    }
}
