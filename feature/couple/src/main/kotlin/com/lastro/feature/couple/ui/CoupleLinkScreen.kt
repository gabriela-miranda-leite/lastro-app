package com.lastro.feature.couple.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.LastroAvatarOverlap
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroSegmentedControl
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.LastroTopBar
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.couple.viewmodel.CoupleLinkEvent
import com.lastro.feature.couple.viewmodel.CoupleLinkUiState
import com.lastro.feature.couple.viewmodel.CoupleLinkViewModel
import android.content.Intent

@Composable
fun CoupleLinkScreen(
    onBack: () -> Unit,
    onLinked: () -> Unit,
    viewModel: CoupleLinkViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CoupleLinkEvent.ShareInvite -> {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Use o código ${event.code} no app Lastro para vincularmos nossas finanças! O código expira em 24h.",
                        )
                        if (uiState.partnerEmail.isNotBlank()) {
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(uiState.partnerEmail))
                        }
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Enviar convite"))
                }
                CoupleLinkEvent.LinkedSuccess -> onLinked()
            }
        }
    }

    CoupleLinkContent(
        uiState = uiState,
        onBack = onBack,
        onSkip = onBack,
        onTabSelected = viewModel::onTabSelected,
        onPartnerEmailChange = viewModel::onPartnerEmailChange,
        onInviteCodeChange = viewModel::onInviteCodeChange,
        onSendInvite = viewModel::sendInvite,
        onJoinCouple = viewModel::joinCouple,
    )
}

@Composable
internal fun CoupleLinkContent(
    uiState: CoupleLinkUiState,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onPartnerEmailChange: (String) -> Unit,
    onInviteCodeChange: (String) -> Unit,
    onSendInvite: () -> Unit,
    onJoinCouple: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            LastroTopBar(title = "Vincular casal", onNavigateBack = onBack)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))

            LastroAvatarOverlap(
                initials = listOf("G", "M"),
                size = 52.dp,
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Finanças a dois",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Vincule-se ao seu parceiro para compartilhar\ndespesas e visualizar o saldo conjunto.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(24.dp))

            LastroSegmentedControl(
                options = listOf("Convidar parceiro", "Tenho um convite"),
                selectedIndex = uiState.selectedTab,
                onOptionSelected = onTabSelected,
            )

            Spacer(Modifier.height(24.dp))

            if (uiState.selectedTab == 0) {
                InviteTab(
                    email = uiState.partnerEmail,
                    generatedInvite = uiState.generatedInvite,
                    onEmailChange = onPartnerEmailChange,
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onSendInvite = onSendInvite,
                    onSkip = onSkip,
                )
            } else {
                JoinTab(
                    code = uiState.inviteCode,
                    onCodeChange = onInviteCodeChange,
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onJoin = onJoinCouple,
                    onSkip = onSkip,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InviteTab(
    email: String,
    generatedInvite: CoupleInvite?,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onSendInvite: () -> Unit,
    onSkip: () -> Unit,
) {
    LastroTextField(
        value = email,
        onValueChange = onEmailChange,
        label = "Email do parceiro",
        type = TextFieldType.Email,
        error = error,
    )

    Spacer(Modifier.height(20.dp))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InviteStep(number = 1, text = "Insira o email do seu parceiro acima.")
        InviteStep(number = 2, text = "Ele receberá um link de convite por email.")
        InviteStep(
            number = 3,
            text = "Após aceitar, vocês passam a ver despesas compartilhadas e o saldo do casal.",
            highlight = "Após aceitar",
        )
    }

    if (generatedInvite != null) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.medium,
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Código gerado: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = generatedInvite.code,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Expira em 24h",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    Spacer(Modifier.height(32.dp))

    LastroButton(
        text = "Enviar convite",
        onClick = onSendInvite,
        loading = isLoading,
        enabled = email.isNotBlank(),
    )

    Spacer(Modifier.height(8.dp))

    LastroButton(
        text = "Pular por agora",
        onClick = onSkip,
        variant = ButtonVariant.Ghost,
    )
}

@Composable
private fun JoinTab(
    code: String,
    onCodeChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onJoin: () -> Unit,
    onSkip: () -> Unit,
) {
    LastroTextField(
        value = code,
        onValueChange = { onCodeChange(it.uppercase()) },
        label = "Código do convite",
        error = error,
    )

    Spacer(Modifier.height(20.dp))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InviteStep(number = 1, text = "Cole o código que chegou no seu email.")
        InviteStep(
            number = 2,
            text = "Confirme para aceitar o convite do seu parceiro.",
        )
        InviteStep(
            number = 3,
            text = "Pronto! Vocês passam a ver despesas compartilhadas e o saldo do casal.",
            highlight = "Pronto!",
        )
    }

    Spacer(Modifier.height(32.dp))

    LastroButton(
        text = "Entrar no casal",
        onClick = onJoin,
        loading = isLoading,
        enabled = code.isNotBlank(),
    )

    Spacer(Modifier.height(8.dp))

    LastroButton(
        text = "Pular por agora",
        onClick = onSkip,
        variant = ButtonVariant.Ghost,
    )
}

@Composable
private fun InviteStep(
    number: Int,
    text: String,
    highlight: String? = null,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        if (highlight != null) {
            val parts = text.split(highlight, limit = 2)
            Row {
                if (parts.isNotEmpty()) {
                    Text(
                        text = highlight,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                if (parts.size > 1) {
                    Text(
                        text = parts[1],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(name = "CoupleLinkScreen — Convidar — Dark")
@Composable
private fun PreviewInviteDark() = LastroTheme {
    CoupleLinkContent(
        uiState = CoupleLinkUiState(selectedTab = 0),
        onBack = {}, onSkip = {}, onTabSelected = {},
        onPartnerEmailChange = {}, onInviteCodeChange = {},
        onSendInvite = {}, onJoinCouple = {},
    )
}

@Preview(name = "CoupleLinkScreen — Convidar preenchido — Dark")
@Composable
private fun PreviewInviteFilledDark() = LastroTheme {
    CoupleLinkContent(
        uiState = CoupleLinkUiState(
            selectedTab = 0,
            partnerEmail = "marlon@email.com",
            generatedInvite = CoupleInvite(code = "LASTRO-A3F8", expiresAt = "2026-10-27T20:00:00Z"),
        ),
        onBack = {}, onSkip = {}, onTabSelected = {},
        onPartnerEmailChange = {}, onInviteCodeChange = {},
        onSendInvite = {}, onJoinCouple = {},
    )
}

@Preview(name = "CoupleLinkScreen — Tenho convite — Dark")
@Composable
private fun PreviewJoinDark() = LastroTheme {
    CoupleLinkContent(
        uiState = CoupleLinkUiState(selectedTab = 1),
        onBack = {}, onSkip = {}, onTabSelected = {},
        onPartnerEmailChange = {}, onInviteCodeChange = {},
        onSendInvite = {}, onJoinCouple = {},
    )
}

@Preview(name = "CoupleLinkScreen — Erro expirado — Dark")
@Composable
private fun PreviewJoinErrorDark() = LastroTheme {
    CoupleLinkContent(
        uiState = CoupleLinkUiState(
            selectedTab = 1,
            inviteCode = "LASTRO-XXXX",
            error = "Este código expirou ou já foi utilizado.",
        ),
        onBack = {}, onSkip = {}, onTabSelected = {},
        onPartnerEmailChange = {}, onInviteCodeChange = {},
        onSendInvite = {}, onJoinCouple = {},
    )
}

@Preview(name = "CoupleLinkScreen — Convidar — Light")
@Composable
private fun PreviewInviteLight() = LastroTheme(darkTheme = false) {
    CoupleLinkContent(
        uiState = CoupleLinkUiState(selectedTab = 0, partnerEmail = "marlon@email.com"),
        onBack = {}, onSkip = {}, onTabSelected = {},
        onPartnerEmailChange = {}, onInviteCodeChange = {},
        onSendInvite = {}, onJoinCouple = {},
    )
}

@Preview(name = "CoupleLinkScreen — Tenho convite — Light")
@Composable
private fun PreviewJoinLight() = LastroTheme(darkTheme = false) {
    CoupleLinkContent(
        uiState = CoupleLinkUiState(selectedTab = 1, inviteCode = "LASTRO-A3F8"),
        onBack = {}, onSkip = {}, onTabSelected = {},
        onPartnerEmailChange = {}, onInviteCodeChange = {},
        onSendInvite = {}, onJoinCouple = {},
    )
}
