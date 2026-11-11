package com.lastro.feature.auth.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.testing.repository.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResetPasswordViewModelTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: ResetPasswordViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeAuthRepository()
        viewModel = ResetPasswordViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is not loading and not terminal`() {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.success)
        assertFalse(state.invalidToken)
    }

    @Test
    fun `submit success sets success state`() = runTest {
        repository.resetPasswordResult = Result.Success(Unit)

        viewModel.submit("valid-token", "NovaSenha123!")

        val state = viewModel.uiState.value
        assertTrue(state.success)
        assertFalse(state.invalidToken)
        assertFalse(state.isLoading)
    }

    @Test
    fun `submit error sets invalid token state`() = runTest {
        repository.resetPasswordResult = Result.Error(RuntimeException("invalid token"), "invalid token")

        viewModel.submit("expired-token", "NovaSenha123!")

        val state = viewModel.uiState.value
        assertTrue(state.invalidToken)
        assertFalse(state.success)
        assertFalse(state.isLoading)
    }

    @Test
    fun `submit success after invalid token clears it`() = runTest {
        repository.resetPasswordResult = Result.Error(RuntimeException(), null)
        viewModel.submit("bad-token", "NovaSenha123!")

        repository.resetPasswordResult = Result.Success(Unit)
        viewModel.submit("valid-token", "NovaSenha123!")

        val state = viewModel.uiState.value
        assertTrue(state.success)
        assertFalse(state.invalidToken)
    }
}
