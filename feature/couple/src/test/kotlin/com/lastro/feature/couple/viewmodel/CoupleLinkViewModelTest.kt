package com.lastro.feature.couple.viewmodel

import com.lastro.core.common.Result
import com.lastro.core.domain.exception.CoupleException
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.domain.usecase.GenerateCoupleInviteUseCase
import com.lastro.core.domain.usecase.LinkCoupleUseCase
import com.lastro.core.testing.repository.FakeCoupleRepository
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoupleLinkViewModelTest {

    private lateinit var repository: FakeCoupleRepository
    private lateinit var viewModel: CoupleLinkViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeCoupleRepository()
        viewModel = CoupleLinkViewModel(
            generateInvite = GenerateCoupleInviteUseCase(repository),
            linkCouple = LinkCoupleUseCase(repository),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is clean`() {
        val state = viewModel.uiState.value
        assertEquals(0, state.selectedTab)
        assertNull(state.error)
        assertFalse(state.isLoading)
        assertNull(state.generatedInvite)
        assertFalse(state.isLinked)
    }

    @Test
    fun `onTabSelected clears error`() = runTest {
        viewModel.onInviteCodeChange("X")
        repository.joinCoupleResult = Result.Error(CoupleException.InviteExpired)
        viewModel.joinCouple()

        viewModel.onTabSelected(0)

        assertNull(viewModel.uiState.value.error)
        assertEquals(0, viewModel.uiState.value.selectedTab)
    }

    // --- sendInvite ---

    @Test
    fun `sendInvite success stores generatedInvite`() = runTest {
        val invite = CoupleInvite(code = "LASTRO-A3F8", expiresAt = "2026-10-27T20:00:00Z")
        repository.generateInviteResult = Result.Success(invite)

        viewModel.sendInvite()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(invite, state.generatedInvite)
        assertNull(state.error)
    }

    @Test
    fun `sendInvite success emits ShareInvite event`() = runTest {
        val invite = CoupleInvite(code = "LASTRO-A3F8", expiresAt = "2026-10-27T20:00:00Z")
        repository.generateInviteResult = Result.Success(invite)

        val events = mutableListOf<CoupleLinkEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.sendInvite()

        assertTrue(events.any { it is CoupleLinkEvent.ShareInvite && it.code == "LASTRO-A3F8" })
        job.cancel()
    }

    @Test
    fun `sendInvite already linked shows conflict message`() = runTest {
        repository.generateInviteResult = Result.Error(CoupleException.AlreadyLinked)

        viewModel.sendInvite()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("já está vinculado"))
    }

    @Test
    fun `sendInvite generic error shows fallback message`() = runTest {
        repository.generateInviteResult = Result.Error(RuntimeException("timeout"))

        viewModel.sendInvite()

        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    // --- joinCouple ---

    @Test
    fun `joinCouple success emits LinkedSuccess event`() = runTest {
        viewModel.onInviteCodeChange("LASTRO-A3F8")
        repository.joinCoupleResult = Result.Success(Unit)

        val events = mutableListOf<CoupleLinkEvent>()
        val job = launch(testDispatcher) { viewModel.events.toList(events) }

        viewModel.joinCouple()

        assertTrue(events.contains(CoupleLinkEvent.LinkedSuccess))
        assertTrue(viewModel.uiState.value.isLinked)
        job.cancel()
    }

    @Test
    fun `joinCouple invite expired shows expired message`() = runTest {
        viewModel.onInviteCodeChange("LASTRO-XXXX")
        repository.joinCoupleResult = Result.Error(CoupleException.InviteExpired)

        viewModel.joinCouple()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("expirou"))
    }

    @Test
    fun `joinCouple invite not found shows not found message`() = runTest {
        viewModel.onInviteCodeChange("LASTRO-XXXX")
        repository.joinCoupleResult = Result.Error(CoupleException.InviteNotFound)

        viewModel.joinCouple()

        assertNotNull(viewModel.uiState.value.error)
        assertTrue(viewModel.uiState.value.error!!.contains("não encontrado"))
    }

    @Test
    fun `joinCouple already linked shows conflict message`() = runTest {
        viewModel.onInviteCodeChange("LASTRO-A3F8")
        repository.joinCoupleResult = Result.Error(CoupleException.AlreadyLinked)

        viewModel.joinCouple()

        assertNotNull(viewModel.uiState.value.error)
        assertTrue(viewModel.uiState.value.error!!.contains("já está vinculado"))
    }

    @Test
    fun `joinCouple blank code shows validation error`() = runTest {
        viewModel.onInviteCodeChange("")

        viewModel.joinCouple()

        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isLinked)
    }

    @Test
    fun `onInviteCodeChange clears error`() = runTest {
        viewModel.onInviteCodeChange("LASTRO-XXXX")
        repository.joinCoupleResult = Result.Error(CoupleException.InviteExpired)
        viewModel.joinCouple()
        assertNotNull(viewModel.uiState.value.error)

        viewModel.onInviteCodeChange("LASTRO-A3F8")

        assertNull(viewModel.uiState.value.error)
    }
}
