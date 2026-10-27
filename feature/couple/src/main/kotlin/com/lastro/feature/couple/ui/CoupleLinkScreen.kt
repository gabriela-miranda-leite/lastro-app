package com.lastro.feature.couple.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.domain.model.CoupleInvite
import com.lastro.core.ui.components.ButtonVariant
import com.lastro.core.ui.components.LastroAvatar
import com.lastro.core.ui.components.LastroButton
import com.lastro.core.ui.components.LastroSegmentedControl
import com.lastro.core.ui.components.LastroTextField
import com.lastro.core.ui.components.LastroTopBar
import com.lastro.core.ui.components.TextFieldType
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.couple.viewmodel.CoupleLinkEvent
import com.lastro.feature.couple.viewmodel.CoupleLinkUiState
import com.lastro.feature.couple.viewmodel.CoupleLinkViewModel

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

            CoupleAvatarHero()

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
    val inviteSent = generatedInvite != null

    if (inviteSent) {
        InviteSentCard()
        Spacer(Modifier.height(32.dp))
        LastroButton(text = "Ir para o dashboard", onClick = onSkip)
    } else {
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
}

@Composable
private fun InviteSentCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                MaterialTheme.shapes.medium,
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                MaterialTheme.shapes.medium,
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Convite enviado!",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                text = "Seu parceiro receberá um email com instruções para entrar. O vínculo é ativado quando ele aceitar.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        val onSurface = MaterialTheme.colorScheme.onSurfaceVariant
        val onBg = MaterialTheme.colorScheme.onBackground
        val annotated = buildAnnotatedString {
            if (highlight != null) {
                val parts = text.split(highlight, limit = 2)
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = onBg)) {
                    append(highlight)
                }
                if (parts.size > 1) {
                    withStyle(SpanStyle(color = onSurface)) {
                        append(parts[1])
                    }
                }
            } else {
                withStyle(SpanStyle(color = onSurface)) {
                    append(text)
                }
            }
        }

        Text(
            text = annotated,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CoupleAvatarHero(modifier: Modifier = Modifier) {
    val diagonalStart = Offset(0f, 0f)
    val diagonalEnd = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    val brushG = Brush.linearGradient(
        colors = listOf(LastroColors.Accent, LastroColors.Positive),
        start = diagonalStart, end = diagonalEnd,
    )
    val brushM = Brush.linearGradient(
        colors = listOf(Color(0xFFFF7C6B), Color(0xFFFFD06B)),
        start = diagonalStart, end = diagonalEnd,
    )
    // Box 144×67dp. Drawing order: G (back) → M → badge (front, z-index:1 no design)
    // G: x=0,y=3 | M: x=80,y=3 | badge: x=56,y=19 — badge na frente de ambos
    Box(modifier = modifier.size(width = 144.dp, height = 67.dp)) {
        LastroAvatar(
            initial = "G",
            size = 64.dp,
            brush = brushG,
            modifier = Modifier.offset(y = 3.dp),
        )
        LastroAvatar(
            initial = "M",
            size = 64.dp,
            brush = brushM,
            modifier = Modifier.offset(x = 80.dp, y = 3.dp),
        )
        Box(
            modifier = Modifier
                .offset(x = 56.dp, y = 19.dp)
                .size(32.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp),
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

@Preview(name = "CoupleLinkScreen — Convite enviado — Dark")
@Composable
private fun PreviewInviteSentDark() = LastroTheme {
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
