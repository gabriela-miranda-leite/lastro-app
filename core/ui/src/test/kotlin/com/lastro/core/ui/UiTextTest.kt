package com.lastro.core.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class UiTextTest {

    @Test
    fun `Literal stores value`() {
        val text = UiText.Literal("hello")
        assertEquals("hello", text.value)
    }

    @Test
    fun `Literal equality`() {
        assertEquals(UiText.Literal("a"), UiText.Literal("a"))
        assertNotEquals(UiText.Literal("a"), UiText.Literal("b"))
    }

    @Test
    fun `Res stores id`() {
        val text = UiText.Res(42)
        assertEquals(42, text.id)
    }

    @Test
    fun `Res args defaults to empty list`() {
        val text = UiText.Res(1)
        assertEquals(emptyList<Any>(), text.args)
    }

    @Test
    fun `Res stores args`() {
        val text = UiText.Res(1, listOf("foo", 99))
        assertEquals(listOf("foo", 99), text.args)
    }

    @Test
    fun `Res equality`() {
        assertEquals(UiText.Res(10), UiText.Res(10))
        assertNotEquals(UiText.Res(10), UiText.Res(20))
        assertNotEquals(UiText.Res(10, listOf("a")), UiText.Res(10))
    }

    @Test
    fun `Literal and Res are not equal`() {
        assertNotEquals(UiText.Literal("x") as Any, UiText.Res(0) as Any)
    }
}
