package com.lastro.feature.auth.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.network.exception.ApiException
import com.lastro.core.testing.repository.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ForgotPasswordViewModelTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeAuthRepository()
        viewModel = ForgotPasswordViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.success)
    }

    @Test
    fun `submit success sets success true`() = runTest {
        repository.forgotPasswordResult = Result.Success(Unit)

        viewModel.submit("gabriela@lastro.app")

        val state = viewModel.uiState.value
        assertTrue(state.success)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `submit 429 shows rate limit message`() = runTest {
        repository.forgotPasswordResult = Result.Error(
            ApiException.TooManyRequests,
            ApiException.TooManyRequests.message,
        )

        viewModel.submit("gabriela@lastro.app")

        assertEquals(
            "Muitas tentativas. Aguarde alguns minutos",
            viewModel.uiState.value.error,
        )
    }

    @Test
    fun `submit generic error shows fallback message`() = runTest {
        repository.forgotPasswordResult = Result.Error(RuntimeException("server error"), "server error")

        viewModel.submit("gabriela@lastro.app")

        assertEquals(
            "Não foi possível enviar o link. Tente novamente",
            viewModel.uiState.value.error,
        )
    }

    @Test
    fun `submit sets isLoading during request`() = runTest {
        repository.forgotPasswordResult = Result.Success(Unit)
        viewModel.submit("gabriela@lastro.app")
        assertFalse(viewModel.uiState.value.isLoading)
    }
}
