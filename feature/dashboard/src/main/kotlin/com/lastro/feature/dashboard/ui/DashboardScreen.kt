package com.lastro.feature.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.ui.UiText
import com.lastro.core.ui.resolve
import com.lastro.core.ui.components.BannerType
import com.lastro.core.ui.components.BottomNavItem
import com.lastro.core.ui.components.ChartAxisLabel
import com.lastro.core.ui.components.ChartPoint
import com.lastro.core.ui.components.DonutSlice
import com.lastro.core.ui.components.EmptyState
import com.lastro.core.ui.components.ErrorBanner
import com.lastro.core.ui.components.IconWrapVariant
import com.lastro.core.ui.components.LastroAvatar
import com.lastro.core.ui.components.LastroBanner
import com.lastro.core.ui.components.LastroBottomNavigation
import com.lastro.core.ui.components.LastroCard
import com.lastro.core.ui.components.LastroDonutChartCard
import com.lastro.core.ui.components.LastroFab
import com.lastro.core.ui.components.LastroIconWrap
import com.lastro.core.ui.components.LastroLineChartCard
import com.lastro.core.ui.components.LastroSegmentedControl
import com.lastro.core.ui.components.LastroSkeletonBlock
import com.lastro.core.ui.components.LastroSkeletonCircle
import com.lastro.core.ui.components.LastroSkeletonRow
import com.lastro.core.ui.icon.LastroIcons
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroIconSize
import com.lastro.core.ui.theme.LastroIconWrapSize
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.dashboard.viewmodel.DashboardRecentTransaction
import com.lastro.feature.dashboard.viewmodel.DashboardUiState
import com.lastro.feature.dashboard.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onNavigateToTransactions: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNewTransaction: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreenContent(
        uiState = uiState,
        onPreviousMonth = viewModel::onPreviousMonth,
        onNextMonth = viewModel::onNextMonth,
        onScopeChange = viewModel::onScopeChange,
        onRetry = viewModel::retry,
        onProfileClick = onNavigateToProfile,
        onNewTransaction = onNavigateToNewTransaction,
        onSeeAllTransactions = onNavigateToTransactions,
        onTabSelected = { index ->
            when (index) {
                1 -> onNavigateToTransactions()
                else -> Unit
            }
        },
    )
}

@Composable
internal fun DashboardScreenContent(
    uiState: DashboardUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onScopeChange: (Boolean) -> Unit,
    onRetry: () -> Unit,
    onProfileClick: () -> Unit,
    onNewTransaction: () -> Unit,
    onSeeAllTransactions: () -> Unit,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            LastroBottomNavigation(
                items = listOf(
                    BottomNavItem("Início", LastroIcons.Dashboard, "Início"),
                    BottomNavItem("Transações", LastroIcons.List, "Transações"),
                    BottomNavItem("Fixos", LastroIcons.Repeat, "Fixos"),
                    BottomNavItem("Perfil", LastroIcons.UserCircle, "Perfil"),
                ),
                selectedIndex = 0,
                onItemSelected = onTabSelected,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            LastroFab(onClick = onNewTransaction, contentDescription = "Nova transação")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = LastroSpacing.screenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            DashboardHeader(
                monthLabel = uiState.monthLabel,
                userInitial = uiState.userInitial,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onProfileClick = onProfileClick,
            )

            if (uiState.showCoupleToggle) {
                Spacer(modifier = Modifier.height(LastroSpacing.lg))
                LastroSegmentedControl(
                    options = listOf("Você", "Casal"),
                    selectedIndex = if (uiState.isCoupleScope) 1 else 0,
                    onOptionSelected = { index -> onScopeChange(index == 1) },
                )
            }

            Spacer(modifier = Modifier.height(LastroSpacing.xl))

            when {
                uiState.isLoading -> DashboardSkeleton()
                uiState.error != null -> ErrorBanner(
                    message = uiState.error.resolve(),
                    visible = true,
                    onRetry = onRetry,
                )
                !uiState.hasData -> Column(verticalArrangement = Arrangement.spacedBy(LastroSpacing.lg)) {
                    DashboardBalanceCard(uiState = uiState)
                    LastroCard {
                        EmptyState(
                            icon = Icons.Default.Add,
                            title = "Nenhuma transação",
                            description = "Adicione sua primeira transação para começar a acompanhar suas finanças.",
                            ctaLabel = "Adicionar transação",
                            onCtaClick = onNewTransaction,
                            iconTint = LastroColors.Accent,
                            iconBackground = LastroColors.Accent.copy(alpha = 0.18f),
                        )
                    }
                }
                else -> DashboardContent(uiState = uiState, onSeeAllTransactions = onSeeAllTransactions)
            }

            Spacer(modifier = Modifier.height(LastroSpacing.huge))
        }
    }
}

@Composable
private fun DashboardHeader(
    monthLabel: String,
    userInitial: String,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics(mergeDescendants = true) {
                    customActions = listOf(
                        CustomAccessibilityAction("Mês anterior") { onPreviousMonth(); true },
                        CustomAccessibilityAction("Próximo mês") { onNextMonth(); true },
                    )
                },
        ) {
            Text(
                text = "Olá, Gabi 👋",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = monthLabel,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        LastroAvatar(
            initial = userInitial,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onProfileClick)
                .semantics { contentDescription = "Ir para o perfil" },
        )
    }
}

@Composable
private fun DashboardSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(LastroSpacing.lg)) {
        LastroCard {
            LastroSkeletonRow(modifier = Modifier.width(110.dp), height = 12.dp)
            Spacer(modifier = Modifier.height(LastroSpacing.md))
            LastroSkeletonRow(modifier = Modifier.width(180.dp), height = 30.dp)
            Spacer(modifier = Modifier.height(LastroSpacing.lg))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(LastroSpacing.md))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    LastroSkeletonRow(modifier = Modifier.width(60.dp), height = 10.dp)
                    Spacer(modifier = Modifier.height(LastroSpacing.xs))
                    LastroSkeletonRow(modifier = Modifier.width(90.dp), height = 16.dp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    LastroSkeletonRow(modifier = Modifier.width(60.dp), height = 10.dp)
                    Spacer(modifier = Modifier.height(LastroSpacing.xs))
                    LastroSkeletonRow(modifier = Modifier.width(90.dp), height = 16.dp)
                }
            }
        }
        LastroCard {
            LastroSkeletonRow(modifier = Modifier.width(150.dp), height = 14.dp)
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LastroSkeletonCircle(size = 90.dp)
                Spacer(modifier = Modifier.width(LastroSpacing.xl))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(LastroSpacing.md),
                ) {
                    LastroSkeletonRow(modifier = Modifier.fillMaxWidth(), height = 12.dp)
                    LastroSkeletonRow(modifier = Modifier.fillMaxWidth(0.8f), height = 12.dp)
                    LastroSkeletonRow(modifier = Modifier.fillMaxWidth(0.9f), height = 12.dp)
                    LastroSkeletonRow(modifier = Modifier.fillMaxWidth(0.65f), height = 12.dp)
                }
            }
        }
        LastroCard {
            LastroSkeletonRow(modifier = Modifier.width(130.dp), height = 14.dp)
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            LastroSkeletonBlock(height = 100.dp, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun DashboardContent(uiState: DashboardUiState, onSeeAllTransactions: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(LastroSpacing.lg)) {
        if (uiState.budgetAlert != null) {
            LastroBanner(message = uiState.budgetAlert.resolve(), type = BannerType.Warning)
        }

        DashboardBalanceCard(uiState = uiState)

        LastroDonutChartCard(
            title = "Gastos por categoria",
            titleTrailing = uiState.monthLabel.substringBefore(" "),
            slices = uiState.donutSlices,
            centerLabel = uiState.donutCenterLabel,
        )

        if (uiState.dailyBalancePoints.size >= 2) {
            LastroLineChartCard(
                title = "Saldo diário",
                points = uiState.dailyBalancePoints,
                highlightIndex = uiState.dailyBalanceHighlightIndex,
                axisLabels = uiState.dailyBalanceAxisLabels,
            )
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DashboardSectionTitle(text = "Recentes")
                Text(
                    text = "Ver todas",
                    style = MaterialTheme.typography.labelMedium,
                    color = LastroColors.Accent,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable(onClick = onSeeAllTransactions),
                )
            }
            Spacer(modifier = Modifier.height(LastroSpacing.md))
            LastroCard {
                uiState.recentTransactions.forEachIndexed { index, tx ->
                    DashboardTransactionRow(tx)
                    if (index != uiState.recentTransactions.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardBalanceCard(uiState: DashboardUiState) {
    val balanceColor = if (uiState.isBalancePositive) LastroColors.Positive else LastroColors.Negative
    LastroCard {
        Text(
            text = uiState.balanceLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xs))
        Text(
            text = uiState.balanceValue,
            style = MaterialTheme.typography.headlineMedium,
            color = balanceColor,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.lg))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(LastroSpacing.md))
        Row(modifier = Modifier.fillMaxWidth()) {
            DashboardBalanceStat(
                modifier = Modifier.weight(1f),
                label = "Receitas",
                value = uiState.incomeValue,
                iconRes = LastroIcons.ArrowUp,
                color = LastroColors.Positive,
            )
            VerticalDivider(
                modifier = Modifier.height(32.dp),
                color = MaterialTheme.colorScheme.outline,
            )
            DashboardBalanceStat(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = LastroSpacing.lg),
                label = "Despesas",
                value = uiState.expenseValue,
                iconRes = LastroIcons.ArrowDown,
                color = LastroColors.Negative,
            )
        }
    }
}

@Composable
private fun DashboardBalanceStat(
    label: String,
    value: String,
    iconRes: Int,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(10.dp),
            )
            Spacer(modifier = Modifier.width(LastroSpacing.xs))
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    }
}

@Composable
private fun DashboardSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun DashboardTransactionRow(tx: DashboardRecentTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = LastroSpacing.minTouchTarget)
            .padding(vertical = LastroSpacing.listItemVertical),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LastroIconWrap(
            painter = painterResource(tx.iconRes),
            variant = tx.iconVariant,
            wrapSize = LastroIconWrapSize.sm,
            iconSize = LastroIconSize.sm,
        )
        Spacer(modifier = Modifier.width(LastroSpacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = tx.name, style = MaterialTheme.typography.bodyLarge)
                if (tx.ownerLabel != null) {
                    Spacer(modifier = Modifier.width(LastroSpacing.xs))
                    DashboardOwnerBadge(label = tx.ownerLabel)
                }
            }
            Text(
                text = tx.meta,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = if (tx.isPositive) "+${tx.amount}" else "−${tx.amount}",
            style = MaterialTheme.typography.titleMedium,
            color = if (tx.isPositive) LastroColors.Positive else LastroColors.Negative,
        )
    }
}

@Composable
private fun DashboardOwnerBadge(label: String) {
    val background = when (label) {
        "G" -> LastroColors.Accent
        "M" -> LastroColors.Wishes
        else -> LastroColors.Neutral
    }
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .background(background, MaterialTheme.shapes.extraSmall)
            .padding(horizontal = LastroSpacing.xs, vertical = 1.dp),
    )
}

@Preview(name = "Dashboard - Carregando")
@Composable
private fun DashboardLoadingPreview() = LastroTheme {
    DashboardScreenContent(
        uiState = DashboardUiState(isLoading = true),
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

@Preview(name = "Dashboard - Vazio")
@Composable
private fun DashboardEmptyPreview() = LastroTheme {
    DashboardScreenContent(
        uiState = PreviewDashboardStates.empty,
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

@Preview(name = "Dashboard - Erro")
@Composable
private fun DashboardErrorPreview() = LastroTheme {
    DashboardScreenContent(
        uiState = DashboardUiState(
            isLoading = false,
            monthLabel = "Outubro 2026",
            error = UiText.Literal("Erro ao carregar suas transações."),
        ),
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

@Preview(name = "Dashboard - Individual")
@Composable
private fun DashboardIndividualPreview() = LastroTheme {
    DashboardScreenContent(
        uiState = PreviewDashboardStates.individual,
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

@Preview(name = "Dashboard - Casal")
@Composable
private fun DashboardCouplePreview() = LastroTheme {
    DashboardScreenContent(
        uiState = PreviewDashboardStates.couple,
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

@Preview(name = "Dashboard - Sem parceiro")
@Composable
private fun DashboardNoPartnerPreview() = LastroTheme {
    DashboardScreenContent(
        uiState = PreviewDashboardStates.noPartner,
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

@Preview(name = "Dashboard - Individual - Light")
@Composable
private fun DashboardIndividualLightPreview() = LastroTheme(darkTheme = false) {
    DashboardScreenContent(
        uiState = PreviewDashboardStates.individual,
        onPreviousMonth = {}, onNextMonth = {}, onScopeChange = {}, onRetry = {},
        onProfileClick = {}, onNewTransaction = {}, onSeeAllTransactions = {}, onTabSelected = {},
    )
}

internal object PreviewDashboardStates {
    val empty = DashboardUiState(
        isLoading = false,
        hasData = false,
        monthLabel = "Outubro 2026",
        balanceLabel = "Saldo do mês",
        balanceValue = "R$ 0,00",
        isBalancePositive = true,
        incomeValue = "R$ 0,00",
        expenseValue = "R$ 0,00",
    )


    val individual = DashboardUiState(
        isLoading = false,
        monthLabel = "Outubro 2026",
        userInitial = "G",
        showCoupleToggle = true,
        isCoupleScope = false,
        hasData = true,
        balanceLabel = "Saldo do mês",
        balanceValue = "R$ 1.240,00",
        isBalancePositive = true,
        incomeValue = "R$ 5.000,00",
        expenseValue = "R$ 3.760,00",
        donutSlices = listOf(
            DonutSlice("Necessidade", 1579f, LastroColors.Accent),
            DonutSlice("Lazer", 1053f, LastroColors.Wishes),
            DonutSlice("Invest.", 677f, LastroColors.Warning),
            DonutSlice("Metas", 451f, LastroColors.Positive),
        ),
        donutCenterLabel = "R$3,7k",
        dailyBalancePoints = (1..31).map { day ->
            val progress = (day - 1) / 30f
            val eased = 1f - (1f - progress.coerceAtMost(0.83f) / 0.83f).let { it * it }
            ChartPoint(day.toString(), 200f + eased * 1040f)
        },
        dailyBalanceAxisLabels = listOf(
            ChartAxisLabel(0, "1 out"),
            ChartAxisLabel(7, "8"),
            ChartAxisLabel(14, "15"),
            ChartAxisLabel(21, "22"),
            ChartAxisLabel(25, "Hoje"),
        ),
        dailyBalanceHighlightIndex = 25,
        recentTransactions = listOf(
            DashboardRecentTransaction("1", "iFood", "Lazer · Hoje", "R$ 45,90", isPositive = false, iconRes = LastroIcons.Heart, iconVariant = IconWrapVariant.Negative),
            DashboardRecentTransaction("2", "Salário", "Receita · Ontem", "R$ 5.000,00", isPositive = true, iconRes = LastroIcons.Wallet, iconVariant = IconWrapVariant.Positive),
            DashboardRecentTransaction("3", "Aluguel", "Necessidade · 14 out", "R$ 1.800,00", isPositive = false, iconRes = LastroIcons.Home, iconVariant = IconWrapVariant.Default),
        ),
    )

    val couple = individual.copy(
        isCoupleScope = true,
        balanceLabel = "Saldo do casal",
        balanceValue = "R$ 6.875,00",
        incomeValue = "R$ 12.200,00",
        expenseValue = "R$ 5.325,00",
        budgetAlert = UiText.Literal("Você usou 85% do orçamento de Desejos este mês."),
        recentTransactions = listOf(
            DashboardRecentTransaction("1", "iFood", "Lazer · Hoje", "R$ 45,90", isPositive = false, iconRes = LastroIcons.Heart, iconVariant = IconWrapVariant.Negative, ownerLabel = "G"),
            DashboardRecentTransaction("2", "Salário Gabi", "Receita · Ontem", "R$ 5.000,00", isPositive = true, iconRes = LastroIcons.Wallet, iconVariant = IconWrapVariant.Positive, ownerLabel = "G"),
            DashboardRecentTransaction("3", "Salário Marlon", "Receita · Ontem", "R$ 7.200,00", isPositive = true, iconRes = LastroIcons.Wallet, iconVariant = IconWrapVariant.Positive, ownerLabel = "M"),
            DashboardRecentTransaction("4", "Aluguel", "Necessidade · 14 out", "R$ 1.800,00", isPositive = false, iconRes = LastroIcons.Home, iconVariant = IconWrapVariant.Default, ownerLabel = "G+M"),
        ),
    )

    val noPartner = individual.copy(showCoupleToggle = false)
}
