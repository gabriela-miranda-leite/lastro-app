package com.lastro.core.ui

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyVisualTransformationTest {

    private val transformation = com.lastro.core.ui.components.moneyVisualTransformationForTest

    @Test
    fun zero_digits_renders_zero() {
        val result = transformation.filter(AnnotatedString("0"))
        assertEquals("R$ 0,00", result.text.text)
    }

    @Test
    fun digits_250_renders_2_50() {
        val result = transformation.filter(AnnotatedString("250"))
        assertEquals("R$ 2,50", result.text.text)
    }

    @Test
    fun digits_25000_renders_250_00() {
        val result = transformation.filter(AnnotatedString("25000"))
        assertEquals("R$ 250,00", result.text.text)
    }

    @Test
    fun digits_1000000_renders_10000_00() {
        val result = transformation.filter(AnnotatedString("1000000"))
        assertEquals("R$ 10.000,00", result.text.text)
    }

    @Test
    fun non_digit_characters_are_ignored() {
        val result = transformation.filter(AnnotatedString("abc250xyz"))
        assertEquals("R$ 2,50", result.text.text)
    }

    @Test
    fun empty_string_renders_zero() {
        val result = transformation.filter(AnnotatedString(""))
        assertEquals("R$ 0,00", result.text.text)
    }
}
