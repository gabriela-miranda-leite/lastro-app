package com.lastro.feature.auth.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.domain.usecase.UpdateUserProfileUseCase
import com.lastro.core.testing.repository.FakeUserRepository
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
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingIncomeViewModelTest {

    private lateinit var repository: FakeUserRepository
    private lateinit var viewModel: OnboardingIncomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeUserRepository()
        viewModel = OnboardingIncomeViewModel(UpdateUserProfileUseCase(repository))
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
        repository.updateMeResult = Result.Success(FakeUserRepository.fakeUser(BigDecimal("5000")))

        val events = mutableListOf<OnboardingIncomeEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.submit(BigDecimal("5000"))

        assertTrue(events.contains(OnboardingIncomeEvent.Success))
        job.cancel()
    }

    @Test
    fun `submit error populates uiState error`() = runTest {
        repository.updateMeResult = Result.Error(RuntimeException("server error"), "server error")

        viewModel.submit(BigDecimal("5000"))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Não foi possível salvar a renda. Tente novamente", state.error)
    }

    @Test
    fun `submit zero income returns validation error before calling repository`() = runTest {
        viewModel.submit(BigDecimal.ZERO)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Não foi possível salvar a renda. Tente novamente", state.error)
    }
}
