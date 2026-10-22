package com.lastro.core.domain.model

import java.math.BigDecimal

data class BudgetBreakdown(
    val type: BudgetType,
    val amount: BigDecimal,
    val percentage: BigDecimal,
    val targetPercentage: BigDecimal,
    val status: BudgetStatus,
)

enum class BudgetStatus {
    UNDER,
    MET,
    OVER,
}
