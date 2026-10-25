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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
    fun `initial state has no error and is not loading`() {
        val state = viewModel.uiState.value
        assertNull(state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `submit success emits Success event`() = runTest {
        repository.resetPasswordResult = Result.Success(Unit)

        val events = mutableListOf<ResetPasswordEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.submit("valid-token", "NovaSenha123!")

        assertTrue(events.contains(ResetPasswordEvent.Success))
        job.cancel()
    }

    @Test
    fun `submit error shows invalid token message`() = runTest {
        repository.resetPasswordResult = Result.Error(RuntimeException("invalid token"), "invalid token")

        viewModel.submit("expired-token", "NovaSenha123!")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Link inválido ou expirado. Solicite um novo link.", state.error)
    }

    @Test
    fun `submit clears previous error on retry`() = runTest {
        repository.resetPasswordResult = Result.Error(RuntimeException(), null)
        viewModel.submit("bad-token", "NovaSenha123!")

        repository.resetPasswordResult = Result.Success(Unit)
        val events = mutableListOf<ResetPasswordEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.submit("valid-token", "NovaSenha123!")

        assertTrue(events.contains(ResetPasswordEvent.Success))
        assertNull(viewModel.uiState.value.error)
        job.cancel()
    }
}
