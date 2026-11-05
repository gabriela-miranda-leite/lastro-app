package com.lastro.feature.dashboard.ui

import android.view.WindowManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastro.core.common.toBRL
import com.lastro.core.domain.model.BudgetBreakdown
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import com.lastro.core.ui.components.BottomNavItem
import com.lastro.core.ui.components.LastroAvatar
import com.lastro.core.ui.components.LastroBottomNavigation
import com.lastro.core.ui.components.LastroCard
import com.lastro.core.ui.components.LastroFab
import com.lastro.core.ui.components.LastroIconWrap
import com.lastro.core.ui.components.IconWrapVariant
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme
import com.lastro.feature.dashboard.viewmodel.DashboardEvent
import com.lastro.feature.dashboard.viewmodel.DashboardUiState
import com.lastro.feature.dashboard.viewmodel.DashboardViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))
private val shortDateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale("pt", "BR"))

private val bottomNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Outlined.GridView, "Dashboard"),
    BottomNavItem("Transações", Icons.Outlined.SwapVert, "Transações"),
    BottomNavItem("Fixos", Icons.Outlined.Schedule, "Fixos"),
    BottomNavItem("Perfil", Icons.Outlined.Person, "Perfil"),
)

@Composable
fun DashboardScreen(
    onNavigateToTransactions: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCouple: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = view.context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                DashboardEvent.NavigateToNewTransaction -> onNavigateToTransactions()
                DashboardEvent.NavigateToAllTransactions -> onNavigateToTransactions()
            }
        }
    }

    DashboardScreenContent(
        uiState = uiState,
        selectedNavIndex = 0,
        onNavSelected = { index ->
            when (index) {
                1 -> onNavigateToTransactions()
                3 -> onNavigateToProfile()
            }
        },
        onToggleBalanceVisibility = viewModel::toggleBalanceVisibility,
        onNewTransaction = viewModel::onNewTransaction,
        onAllTransactions = viewModel::onAllTransactions,
    )
}

@Composable
fun DashboardScreenContent(
    uiState: DashboardUiState,
    selectedNavIndex: Int = 0,
    onNavSelected: (Int) -> Unit = {},
    onToggleBalanceVisibility: () -> Unit = {},
    onNewTransaction: () -> Unit = {},
    onAllTransactions: () -> Unit = {},
) {
    Scaffold(
        bottomBar = {
            LastroBottomNavigation(
                items = bottomNavItems,
                selectedIndex = selectedNavIndex,
                onItemSelected = onNavSelected,
            )
        },
        floatingActionButton = {
            LastroFab(
                onClick = onNewTransaction,
                contentDescription = "Nova transação",
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            DashboardHeader(
                userName = uiState.userName,
                currentMonth = uiState.currentMonth,
            )
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            BalanceCard(
                balance = uiState.balance,
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                isVisible = uiState.isBalanceVisible,
                hasPartner = uiState.hasPartner,
                onToggleVisibility = onToggleBalanceVisibility,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LastroSpacing.screenHorizontal),
            )
            if (uiState.budgetBreakdown.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LastroSpacing.xl))
                CategoryBreakdownCard(
                    breakdown = uiState.budgetBreakdown,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LastroSpacing.screenHorizontal),
                )
            }
            Spacer(modifier = Modifier.height(LastroSpacing.xl))
            DailyBalanceCard(
                transactions = uiState.recentTransactions,
                currentMonth = uiState.currentMonth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LastroSpacing.screenHorizontal),
            )
            if (uiState.recentTransactions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LastroSpacing.xl))
                RecentTransactionsSection(
                    transactions = uiState.recentTransactions,
                    isVisible = uiState.isBalanceVisible,
                    onSeeAll = onAllTransactions,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(LastroSpacing.xxxl))
        }
    }
}

@Composable
private fun DashboardHeader(
    userName: String,
    currentMonth: YearMonth,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LastroSpacing.screenHorizontal)
            .padding(top = LastroSpacing.xl),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = if (userName.isNotEmpty()) "Olá, $userName" else "Olá",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = currentMonth.format(monthFormatter).replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        if (userName.isNotEmpty()) {
            LastroAvatar(initial = userName)
        }
    }
}

@Composable
private fun BalanceCard(
    balance: BigDecimal,
    totalIncome: BigDecimal,
    totalExpense: BigDecimal,
    isVisible: Boolean,
    hasPartner: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val balanceLabel = if (isVisible) balance.toBRL() else "R\$ ••••"
    val a11yBalance = if (isVisible) "Saldo: ${balance.toBRL()}" else "Saldo oculto"

    LastroCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (hasPartner) "Saldo do casal" else "Saldo do mês",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = if (isVisible) "Ocultar saldo" else "Exibir saldo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            text = balanceLabel,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { contentDescription = a11yBalance },
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        Row(modifier = Modifier.fillMaxWidth()) {
            BalanceStat(
                label = "RECEITAS",
                value = if (isVisible) totalIncome.toBRL() else "R\$ ••••",
                color = LastroColors.Positive,
                modifier = Modifier.weight(1f),
            )
            BalanceStat(
                label = "DESPESAS",
                value = if (isVisible) totalExpense.toBRL() else "R\$ ••••",
                color = LastroColors.Negative,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BalanceStat(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xs))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private data class BreakdownSlice(
    val label: String,
    val color: Color,
    val percentage: Float,
    val amount: BigDecimal,
)

private fun BudgetType.toSliceLabel() = when (this) {
    BudgetType.NEEDS -> "Necessidade"
    BudgetType.WANTS -> "Lazer"
    BudgetType.SAVINGS -> "Poupança"
}

private fun BudgetType.toSliceColor() = when (this) {
    BudgetType.NEEDS -> Color(0xFF6B7CFF)
    BudgetType.WANTS -> Color(0xFFFF8C6B)
    BudgetType.SAVINGS -> Color(0xFFFFD06B)
}

@Composable
private fun CategoryBreakdownCard(
    breakdown: List<BudgetBreakdown>,
    modifier: Modifier = Modifier,
) {
    val total = breakdown.fold(BigDecimal.ZERO) { acc, b -> acc + b.amount }
    val slices = breakdown.map { b ->
        BreakdownSlice(
            label = b.type.toSliceLabel(),
            color = b.type.toSliceColor(),
            percentage = if (total > BigDecimal.ZERO)
                b.amount.divide(total, 4, RoundingMode.HALF_UP).toFloat()
            else 0f,
            amount = b.amount,
        )
    }

    LastroCard(modifier = modifier) {
        Text(
            text = "Gastos por categoria",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                DonutChart(
                    slices = slices,
                    modifier = Modifier.fillMaxSize(),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = total.toBRL(compact = true),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.width(LastroSpacing.xl))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                slices.forEach { slice ->
                    DonutLegendRow(slice = slice)
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    slices: List<BreakdownSlice>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.18f
        val radius = (size.minDimension - strokeWidth) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        val topLeft = Offset(center.x - radius, center.y - radius)
        val arcSize = Size(radius * 2f, radius * 2f)

        var startAngle = -90f
        val gapDeg = 3f

        slices.forEach { slice ->
            val sweep = slice.percentage * 360f - gapDeg
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweep.coerceAtLeast(0f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
            startAngle += slice.percentage * 360f
        }
    }
}

@Composable
private fun DonutLegendRow(slice: BreakdownSlice) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(color = slice.color)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = slice.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = "${(slice.percentage * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun DailyBalanceCard(
    transactions: List<Transaction>,
    currentMonth: YearMonth,
    modifier: Modifier = Modifier,
) {
    val lineColor = LastroColors.Accent

    val dailyPoints = buildDailyBalancePoints(transactions, currentMonth)

    LastroCard(modifier = modifier) {
        Text(
            text = "Saldo diário",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(LastroSpacing.xl))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
        ) {
            if (dailyPoints.size < 2) return@Canvas

            val minVal = dailyPoints.minOf { it.second }
            val maxVal = dailyPoints.maxOf { it.second }
            val range = (maxVal - minVal).coerceAtLeast(1.0)

            fun xOf(i: Int) = i.toFloat() / (dailyPoints.size - 1) * size.width
            fun yOf(v: Double) = size.height - ((v - minVal) / range * size.height * 0.8f + size.height * 0.1f).toFloat()

            val path = Path()
            path.moveTo(xOf(0), yOf(dailyPoints[0].second))
            for (i in 1 until dailyPoints.size) {
                val x0 = xOf(i - 1); val y0 = yOf(dailyPoints[i - 1].second)
                val x1 = xOf(i); val y1 = yOf(dailyPoints[i].second)
                val cx = (x0 + x1) / 2f
                path.cubicTo(cx, y0, cx, y1, x1, y1)
            }

            val fillPath = Path().apply {
                addPath(path)
                lineTo(xOf(dailyPoints.size - 1), size.height)
                lineTo(xOf(0), size.height)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.3f), lineColor.copy(alpha = 0f)),
                ),
            )
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
            )
        }
        Spacer(modifier = Modifier.height(LastroSpacing.sm))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf("1", "8", "15", "22", currentMonth.lengthOfMonth().toString()).forEach { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun buildDailyBalancePoints(
    transactions: List<Transaction>,
    month: YearMonth,
): List<Pair<Int, Double>> {
    val days = month.lengthOfMonth()
    val byDay = transactions.groupBy { it.date.dayOfMonth }
    var running = 0.0
    return (1..days).map { day ->
        val dayTxs = byDay[day] ?: emptyList()
        dayTxs.forEach { tx ->
            if (tx.direction == TransactionType.INCOME) running += tx.amount.toDouble()
            else running -= tx.amount.toDouble()
        }
        day to running
    }
}

@Composable
private fun RecentTransactionsSection(
    transactions: List<Transaction>,
    isVisible: Boolean,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LastroSpacing.screenHorizontal),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Recentes",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            TextButton(onClick = onSeeAll) {
                Text(
                    text = "Ver todas",
                    style = MaterialTheme.typography.labelMedium,
                    color = LastroColors.Accent,
                )
            }
        }
        transactions.forEachIndexed { index, tx ->
            TransactionListItem(
                transaction = tx,
                isVisible = isVisible,
            )
            if (index < transactions.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = LastroSpacing.screenHorizontal),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction,
    isVisible: Boolean,
) {
    val isIncome = transaction.direction == TransactionType.INCOME
    val amountColor = if (isIncome) LastroColors.Positive else LastroColors.Negative
    val amountPrefix = if (isIncome) "+" else "-"
    val amountText = if (isVisible) "$amountPrefix${transaction.amount.toBRL()}" else "R\$ ••••"

    val today = LocalDate.now()
    val dateLabel = when (transaction.date) {
        today -> "Hoje"
        today.minusDays(1) -> "Ontem"
        else -> transaction.date.format(shortDateFormatter)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LastroSpacing.screenHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LastroIconWrap(
            icon = if (isIncome) Icons.Outlined.SwapVert else Icons.Outlined.SwapVert,
            contentDescription = null,
            variant = if (isIncome) IconWrapVariant.Positive else IconWrapVariant.Negative,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description ?: transaction.category?.name ?: "Transação",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "${transaction.category?.name ?: if (isIncome) "Receita" else "Despesa"} · $dateLabel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = amountText,
            style = MaterialTheme.typography.bodyMedium,
            color = amountColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun android.content.Context.findActivity(): android.app.Activity? {
    var context = this
    while (context is android.content.ContextWrapper) {
        if (context is android.app.Activity) return context
        context = context.baseContext
    }
    return null
}

private fun BigDecimal.toBRL(compact: Boolean = false): String {
    if (!compact) return toBRL()
    return when {
        this >= BigDecimal("1000") -> "R\$${(this / BigDecimal("1000")).setScale(1, RoundingMode.HALF_UP)}k"
        else -> toBRL()
    }
}

@Preview(name = "Dashboard - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    DashboardScreenContent(uiState = previewFilledState())
}

@Preview(name = "Dashboard - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    DashboardScreenContent(uiState = previewFilledState())
}

@Preview(name = "Dashboard - Saldo oculto")
@Composable
private fun PreviewHidden() = LastroTheme {
    DashboardScreenContent(uiState = previewFilledState().copy(isBalanceVisible = false))
}

private fun previewFilledState() = DashboardUiState(
    userName = "Gabi",
    hasPartner = true,
    totalIncome = BigDecimal("12200"),
    totalExpense = BigDecimal("5325"),
    balance = BigDecimal("6875"),
)
