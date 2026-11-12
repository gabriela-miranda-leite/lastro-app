package com.lastro.feature.dashboard.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.lastro.core.ui.UiText
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.dashboard.ui.DashboardScreenContent
import com.lastro.feature.dashboard.ui.PreviewDashboardStates
import com.lastro.feature.dashboard.viewmodel.DashboardUiState
import org.junit.Rule
import org.junit.Test

class DashboardScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    private val noop: (Any) -> Unit = {}
    private val noopUnit: () -> Unit = {}

    private fun snapshot(darkTheme: Boolean, uiState: DashboardUiState) = paparazzi.snapshot {
        LastroTheme(darkTheme = darkTheme) {
            DashboardScreenContent(
                uiState = uiState,
                onPreviousMonth = noopUnit,
                onNextMonth = noopUnit,
                onScopeChange = noop,
                onRetry = noopUnit,
                onProfileClick = noopUnit,
                onNewTransaction = noopUnit,
                onSeeAllTransactions = noopUnit,
                onTabSelected = noop,
            )
        }
    }

    @Test
    fun loading_dark() = snapshot(darkTheme = true, uiState = DashboardUiState(isLoading = true))

    @Test
    fun loading_light() = snapshot(darkTheme = false, uiState = DashboardUiState(isLoading = true))

    @Test
    fun empty_dark() = snapshot(darkTheme = true, uiState = PreviewDashboardStates.empty)

    @Test
    fun empty_light() = snapshot(darkTheme = false, uiState = PreviewDashboardStates.empty)

    @Test
    fun error_dark() = snapshot(
        darkTheme = true,
        uiState = DashboardUiState(
            isLoading = false,
            monthLabel = "Outubro 2026",
            error = UiText.Literal("Erro ao carregar suas transações."),
        ),
    )

    @Test
    fun error_light() = snapshot(
        darkTheme = false,
        uiState = DashboardUiState(
            isLoading = false,
            monthLabel = "Outubro 2026",
            error = UiText.Literal("Erro ao carregar suas transações."),
        ),
    )

    @Test
    fun success_individual_dark() = snapshot(darkTheme = true, uiState = PreviewDashboardStates.individual)

    @Test
    fun success_individual_light() = snapshot(darkTheme = false, uiState = PreviewDashboardStates.individual)

    @Test
    fun success_couple_dark() = snapshot(darkTheme = true, uiState = PreviewDashboardStates.couple)

    @Test
    fun success_couple_light() = snapshot(darkTheme = false, uiState = PreviewDashboardStates.couple)

    @Test
    fun success_no_partner_dark() = snapshot(darkTheme = true, uiState = PreviewDashboardStates.noPartner)

    @Test
    fun success_no_partner_light() = snapshot(darkTheme = false, uiState = PreviewDashboardStates.noPartner)
}
