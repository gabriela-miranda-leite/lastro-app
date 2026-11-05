package com.lastro.feature.dashboard.ui

import android.view.WindowManager
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.style.TextOverflow
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
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE) }
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
        onToggleBalanceVisibility = viewModel::toggleBalanceVisibility,
        onNewTransaction = viewModel::onNewTransaction,
        onAllTransactions = viewModel::onAllTransactions,
        onNavSelected = { index ->
            when (index) {
                1 -> onNavigateToTransactions()
                3 -> onNavigateToProfile()
            }
        },
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
            LastroFab(onClick = onNewTransaction, contentDescription = "Nova transação")
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            DashboardHeader(userName = uiState.userName, currentMonth = uiState.currentMonth)
            Spacer(Modifier.height(LastroSpacing.xl))

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
                Spacer(Modifier.height(LastroSpacing.xl))
                CategoryBreakdownCard(
                    breakdown = uiState.budgetBreakdown,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LastroSpacing.screenHorizontal),
                )
            }

            Spacer(Modifier.height(LastroSpacing.xl))
            DailyBalanceCard(
                transactions = uiState.recentTransactions,
                currentMonth = uiState.currentMonth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LastroSpacing.screenHorizontal),
            )

            if (uiState.recentTransactions.isNotEmpty()) {
                Spacer(Modifier.height(LastroSpacing.xl))
                RecentTransactionsSection(
                    transactions = uiState.recentTransactions,
                    isVisible = uiState.isBalanceVisible,
                    currentUserId = uiState.currentUserId,
                    currentUserInitial = uiState.currentUserInitial,
                    partnerInitial = uiState.partnerInitial,
                    onSeeAll = onAllTransactions,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(LastroSpacing.xxxl))
        }
    }
}

@Composable
private fun DashboardHeader(userName: String, currentMonth: YearMonth, modifier: Modifier = Modifier) {
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
                fontWeight = FontWeight.Bold,
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
            text = if (isVisible) balance.toBRL() else "R\$ ••••",
            style = MaterialTheme.typography.displayMedium,
            color = LastroColors.Positive,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { contentDescription = a11yBalance },
        )
        Spacer(Modifier.height(LastroSpacing.xl))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(LastroSpacing.xl))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            BalanceStat(
                label = "RECEITAS",
                value = if (isVisible) totalIncome.toBRL() else "R\$ ••••",
                color = LastroColors.Positive,
                modifier = Modifier.weight(1f),
            )
            VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.width(LastroSpacing.xl))
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
private fun BalanceStat(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(LastroSpacing.xs))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

// ─── Gastos por categoria ────────────────────────────────────────────────────

private data class BreakdownSlice(val label: String, val color: Color, val pct: Float, val amount: BigDecimal)

private fun BudgetType.label() = when (this) {
    BudgetType.NEEDS -> "Necessidade"
    BudgetType.WANTS -> "Lazer"
    BudgetType.INVESTMENTS -> "Investimentos"
    BudgetType.SAVINGS -> "Meta"
}

private fun BudgetType.sliceColor() = when (this) {
    BudgetType.NEEDS -> Color(0xFF6B7CFF)
    BudgetType.WANTS -> Color(0xFFFF8060)
    BudgetType.INVESTMENTS -> Color(0xFFFFD06B)
    BudgetType.SAVINGS -> Color(0xFF4ECBA8)
}

@Composable
private fun CategoryBreakdownCard(breakdown: List<BudgetBreakdown>, modifier: Modifier = Modifier) {
    val total = breakdown.fold(BigDecimal.ZERO) { acc, b -> acc + b.amount }
    val slices = breakdown.map { b ->
        BreakdownSlice(
            label = b.type.label(),
            color = b.type.sliceColor(),
            pct = if (total > BigDecimal.ZERO)
                b.amount.divide(total, 4, RoundingMode.HALF_UP).toFloat()
            else 0f,
            amount = b.amount,
        )
    }

    LastroCard(modifier = modifier) {
        Text(
            text = "Gastos por categoria",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(LastroSpacing.xl))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                DonutChart(slices = slices, modifier = Modifier.fillMaxSize())
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = total.toCompactBRL(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(Modifier.width(LastroSpacing.xl))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                slices.forEach { LegendRow(it) }
            }
        }
    }
}

@Composable
private fun DonutChart(slices: List<BreakdownSlice>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.17f
        val r = (size.minDimension - stroke) / 2f
        val tl = Offset(size.width / 2f - r, size.height / 2f - r)
        val arcSize = Size(r * 2f, r * 2f)
        var start = -90f
        slices.forEach { slice ->
            val sweep = (slice.pct * 360f - 2f).coerceAtLeast(0f)
            drawArc(
                color = slice.color,
                startAngle = start,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = tl,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            start += slice.pct * 360f
        }
    }
}

@Composable
private fun LegendRow(slice: BreakdownSlice) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(slice.color),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = slice.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = "${(slice.pct * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ─── Saldo diário ────────────────────────────────────────────────────────────

@Composable
private fun DailyBalanceCard(
    transactions: List<Transaction>,
    currentMonth: YearMonth,
    modifier: Modifier = Modifier,
) {
    val lineColor = LastroColors.Accent
    val points = buildDailyPoints(transactions, currentMonth)

    LastroCard(modifier = modifier) {
        Text(
            text = "Saldo diário",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(LastroSpacing.xl))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
        ) {
            if (points.size < 2) return@Canvas

            val minV = points.minOf { it.second }
            val maxV = points.maxOf { it.second }
            val range = (maxV - minV).coerceAtLeast(1.0)
            val padV = size.height * 0.12f

            fun xOf(i: Int) = i.toFloat() / (points.size - 1) * size.width
            fun yOf(v: Double) = (size.height - padV) - ((v - minV) / range * (size.height - padV * 2)).toFloat()

            val path = Path().apply {
                moveTo(xOf(0), yOf(points[0].second))
                for (i in 1 until points.size) {
                    val cx = (xOf(i - 1) + xOf(i)) / 2f
                    cubicTo(cx, yOf(points[i - 1].second), cx, yOf(points[i].second), xOf(i), yOf(points[i].second))
                }
            }

            val fillPath = Path().apply {
                addPath(path)
                lineTo(xOf(points.size - 1), size.height)
                lineTo(0f, size.height)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.35f), lineColor.copy(alpha = 0f)),
                ),
            )
            drawPath(path = path, color = lineColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
        }
        Spacer(Modifier.height(LastroSpacing.sm))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("1", "8", "15", "22", currentMonth.lengthOfMonth().toString()).forEach { d ->
                Text(text = d, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private fun buildDailyPoints(transactions: List<Transaction>, month: YearMonth): List<Pair<Int, Double>> {
    val byDay = transactions.groupBy { it.date.dayOfMonth }
    var running = 0.0
    return (1..month.lengthOfMonth()).map { day ->
        (byDay[day] ?: emptyList()).forEach { tx ->
            if (tx.direction == TransactionType.INCOME) running += tx.amount.toDouble()
            else running -= tx.amount.toDouble()
        }
        day to running
    }
}

// ─── Recentes ────────────────────────────────────────────────────────────────

@Composable
private fun RecentTransactionsSection(
    transactions: List<Transaction>,
    isVisible: Boolean,
    currentUserId: String,
    currentUserInitial: String,
    partnerInitial: String?,
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
                fontWeight = FontWeight.SemiBold,
            )
            TextButton(onClick = onSeeAll) {
                Text(text = "Ver todas", style = MaterialTheme.typography.labelMedium, color = LastroColors.Accent)
            }
        }
        transactions.forEachIndexed { index, tx ->
            TransactionListItem(
                transaction = tx,
                isVisible = isVisible,
                currentUserId = currentUserId,
                currentUserInitial = currentUserInitial,
                partnerInitial = partnerInitial,
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
    currentUserId: String,
    currentUserInitial: String,
    partnerInitial: String?,
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

    val categoryName = transaction.category?.name ?: if (isIncome) "Receita" else "Despesa"
    val subtitle = "$categoryName · $dateLabel"

    val userBadge = when {
        transaction.isShared && partnerInitial != null -> "$currentUserInitial+$partnerInitial"
        transaction.userId == currentUserId -> currentUserInitial
        else -> partnerInitial ?: currentUserInitial
    }

    val iconEmoji = transaction.category?.icon
    val iconBgColor = transaction.category?.color?.parseHexColor()
        ?: if (isIncome) LastroColors.Positive.copy(alpha = 0.15f) else LastroColors.Negative.copy(alpha = 0.15f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LastroSpacing.screenHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryIconBox(emoji = iconEmoji, bgColor = iconBgColor)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = transaction.description ?: categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                UserBadge(label = userBadge)
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = amountText,
            style = MaterialTheme.typography.bodyMedium,
            color = amountColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun CategoryIconBox(emoji: String?, bgColor: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        if (emoji != null) {
            Text(text = emoji, fontSize = 18.sp)
        } else {
            Icon(
                imageVector = Icons.Outlined.SwapVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun UserBadge(label: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outlineVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
        )
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun String.parseHexColor(): Color? = try {
    Color(android.graphics.Color.parseColor(this))
} catch (_: Exception) {
    null
}

private fun BigDecimal.toCompactBRL(): String {
    val thousands = this.divide(BigDecimal("1000"), 1, RoundingMode.HALF_UP)
    return if (this >= BigDecimal("1000")) "R\$${thousands}k" else toBRL()
}

private fun android.content.Context.findActivity(): android.app.Activity? {
    var context = this
    while (context is android.content.ContextWrapper) {
        if (context is android.app.Activity) return context
        context = context.baseContext
    }
    return null
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "Dashboard - Dark")
@Composable
private fun PreviewDark() = LastroTheme { DashboardScreenContent(uiState = previewState()) }

@Preview(name = "Dashboard - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) { DashboardScreenContent(uiState = previewState()) }

@Preview(name = "Dashboard - Saldo oculto")
@Composable
private fun PreviewHidden() = LastroTheme { DashboardScreenContent(uiState = previewState().copy(isBalanceVisible = false)) }

private fun previewState() = DashboardUiState(
    userName = "Gabi",
    hasPartner = true,
    currentUserInitial = "G",
    partnerInitial = "M",
    totalIncome = BigDecimal("12200"),
    totalExpense = BigDecimal("5325"),
    balance = BigDecimal("6875"),
)
