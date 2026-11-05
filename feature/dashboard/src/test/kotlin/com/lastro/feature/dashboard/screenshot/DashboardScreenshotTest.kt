package com.lastro.feature.dashboard.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.dashboard.ui.DashboardScreenContent
import com.lastro.feature.dashboard.viewmodel.DashboardUiState
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.YearMonth

class DashboardScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    private val filledState = DashboardUiState(
        userName = "Gabi",
        currentMonth = YearMonth.of(2025, 10),
        totalIncome = BigDecimal("12200"),
        totalExpense = BigDecimal("5325"),
        balance = BigDecimal("6875"),
    )

    @Test fun default_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            DashboardScreenContent(uiState = DashboardUiState())
        }
    }

    @Test fun default_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) {
            DashboardScreenContent(uiState = DashboardUiState())
        }
    }

    @Test fun filled_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            DashboardScreenContent(uiState = filledState)
        }
    }

    @Test fun filled_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) {
            DashboardScreenContent(uiState = filledState)
        }
    }

    @Test fun balance_hidden_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            DashboardScreenContent(uiState = filledState.copy(isBalanceVisible = false))
        }
    }

    @Test fun loading_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            DashboardScreenContent(uiState = DashboardUiState(isLoading = true))
        }
    }
}
