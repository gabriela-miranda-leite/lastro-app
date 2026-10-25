package com.lastro.feature.auth.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.testing.repository.FakeAuthRepository
import com.lastro.core.ui.UiText
import com.lastro.feature.auth.R
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeAuthRepository()
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success emits LoginSuccess event`() = runTest {
        repository.loginResult = Result.Success(Unit)

        val events = mutableListOf<AuthEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.login("email@test.com", "password")

        assertTrue(events.contains(AuthEvent.LoginSuccess))
        job.cancel()
    }

    @Test
    fun `login error populates uiState error`() = runTest {
        repository.loginResult = Result.Error(RuntimeException("invalid credentials"), "invalid credentials")

        viewModel.login("email@test.com", "wrong")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(UiText.Literal("invalid credentials"), state.error)
    }

    @Test
    fun `login error uses fallback message when exception has no message`() = runTest {
        repository.loginResult = Result.Error(RuntimeException(), null)

        viewModel.login("email@test.com", "wrong")

        assertEquals(UiText.Res(R.string.login_error_generic), viewModel.uiState.value.error)
    }

    @Test
    fun `register success emits RegisterSuccess event`() = runTest {
        repository.registerResult = Result.Success(Unit)

        val events = mutableListOf<AuthEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.register("email@test.com", "password", "Name")

        assertTrue(events.contains(AuthEvent.RegisterSuccess))
        job.cancel()
    }

    @Test
    fun `register error populates uiState error`() = runTest {
        repository.registerResult = Result.Error(RuntimeException("email taken"), "email taken")

        viewModel.register("email@test.com", "password", "Name")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(UiText.Literal("email taken"), state.error)
    }

    @Test
    fun `initial state has no error and is not loading`() {
        val state = viewModel.uiState.value
        assertNull(state.error)
        assertFalse(state.isLoading)
    }
}
