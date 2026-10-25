package com.lastro.feature.auth.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.testing.repository.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private lateinit var repository: FakeAuthRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `not logged in navigates to login`() = runTest {
        repository = FakeAuthRepository().apply { loggedIn = false }
        val events = mutableListOf<SplashEvent>()
        val job = launch(testDispatcher) {
            SplashViewModel(repository).events.toList(events)
        }
        testScheduler.advanceUntilIdle()
        assertEquals(SplashEvent.NavigateToLogin, events.first())
        job.cancel()
    }

    @Test
    fun `logged in with valid token navigates to home`() = runTest {
        repository = FakeAuthRepository().apply {
            loggedIn = true
            refreshResult = Result.Success(Unit)
        }
        val events = mutableListOf<SplashEvent>()
        val job = launch(testDispatcher) {
            SplashViewModel(repository).events.toList(events)
        }
        testScheduler.advanceUntilIdle()
        assertEquals(SplashEvent.NavigateToHome, events.first())
        job.cancel()
    }

    @Test
    fun `logged in but refresh fails navigates to login`() = runTest {
        repository = FakeAuthRepository().apply {
            loggedIn = true
            refreshResult = Result.Error(RuntimeException("expired"), "expired")
        }
        val events = mutableListOf<SplashEvent>()
        val job = launch(testDispatcher) {
            SplashViewModel(repository).events.toList(events)
        }
        testScheduler.advanceUntilIdle()
        assertEquals(SplashEvent.NavigateToLogin, events.first())
        job.cancel()
    }
}
