package com.lastro.core.domain.usecase

import com.lastro.core.domain.model.BudgetBreakdown
import com.lastro.core.domain.model.BudgetStatus
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CalculateBudgetBreakdownUseCase @Inject constructor() {

    operator fun invoke(
        income: BigDecimal,
        transactions: List<Transaction>,
    ): List<BudgetBreakdown> {
        if (income <= BigDecimal.ZERO) return emptyList()

        val targets = mapOf(
            BudgetType.NEEDS to BigDecimal("0.50"),
            BudgetType.WANTS to BigDecimal("0.30"),
            BudgetType.SAVINGS to BigDecimal("0.20"),
        )

        val spentByType = transactions
            .filter { it.direction == TransactionType.EXPENSE && it.category != null }
            .groupBy { it.category!!.type }
            .mapValues { (_, txs) -> txs.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount } }

        return targets.map { (type, targetPct) ->
            val targetAmount = income.multiply(targetPct)
            val actual = spentByType[type] ?: BigDecimal.ZERO
            val actualPct = actual.divide(income, 4, RoundingMode.HALF_UP)

            BudgetBreakdown(
                type = type,
                amount = actual,
                percentage = actualPct,
                targetPercentage = targetPct,
                status = when (actual.compareTo(targetAmount)) {
                    -1 -> BudgetStatus.UNDER
                    0 -> BudgetStatus.MET
                    else -> BudgetStatus.OVER
                },
            )
        }
    }
}
