package com.lastro.feature.couple.screenshot

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.couple.ui.CoupleLinkContent
import com.lastro.feature.couple.viewmodel.CoupleLinkUiState
import org.junit.Rule
import org.junit.Test

class CoupleLinkScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
    )

    private val noop: (Any) -> Unit = {}
    private val noopUnit: () -> Unit = {}

    @Test
    fun invite_tab_empty_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(selectedTab = 0),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun invite_tab_filled_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(
                    selectedTab = 0,
                    partnerEmail = "marlon@email.com",
                    generatedInvite = CoupleInvite(code = "LASTRO-A3F8", expiresAt = "2026-10-27T20:00:00Z"),
                ),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun join_tab_empty_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(selectedTab = 1),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun join_tab_filled_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(selectedTab = 1, inviteCode = "LASTRO-A3F8"),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun join_tab_error_expired_dark() = paparazzi.snapshot {
        LastroTheme(darkTheme = true) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(
                    selectedTab = 1,
                    inviteCode = "LASTRO-XXXX",
                    error = "Este código expirou ou já foi utilizado.",
                ),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun invite_tab_empty_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(selectedTab = 0),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun invite_tab_filled_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(
                    selectedTab = 0,
                    partnerEmail = "marlon@email.com",
                    generatedInvite = CoupleInvite(code = "LASTRO-A3F8", expiresAt = "2026-10-27T20:00:00Z"),
                ),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun join_tab_filled_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(selectedTab = 1, inviteCode = "LASTRO-A3F8"),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }

    @Test
    fun join_tab_error_expired_light() = paparazzi.snapshot {
        LastroTheme(darkTheme = false) {
            CoupleLinkContent(
                uiState = CoupleLinkUiState(
                    selectedTab = 1,
                    inviteCode = "LASTRO-XXXX",
                    error = "Este código expirou ou já foi utilizado.",
                ),
                onBack = noopUnit, onSkip = noopUnit, onTabSelected = noop,
                onPartnerEmailChange = noop, onInviteCodeChange = noop,
                onSendInvite = noopUnit, onJoinCouple = noopUnit,
            )
        }
    }
}
