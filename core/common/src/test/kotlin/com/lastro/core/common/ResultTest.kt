package com.lastro.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `mapSuccess transforms Success value`() {
        val result = Result.Success(2).mapSuccess { it * 3 }
        assertEquals(Result.Success(6), result)
    }

    @Test
    fun `mapSuccess propagates Error unchanged`() {
        val error = Result.Error(RuntimeException("fail"))
        val mapped = error.mapSuccess { "ignored" }
        assertEquals(error, mapped)
    }

    @Test
    fun `mapSuccess propagates Loading unchanged`() {
        val mapped = Result.Loading.mapSuccess { "ignored" }
        assertEquals(Result.Loading, mapped)
    }

    @Test
    fun `onSuccess invokes action on Success`() {
        var called = false
        Result.Success("value").onSuccess { called = true }
        assertTrue(called)
    }

    @Test
    fun `onSuccess skips action on Error`() {
        var called = false
        Result.Error(RuntimeException()).onSuccess { called = true }
        assertFalse(called)
    }

    @Test
    fun `onError invokes action on Error`() {
        var called = false
        Result.Error(RuntimeException()).onError { called = true }
        assertTrue(called)
    }

    @Test
    fun `onError skips action on Success`() {
        var called = false
        Result.Success("value").onError { called = true }
        assertFalse(called)
    }
}
