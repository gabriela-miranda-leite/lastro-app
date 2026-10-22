package com.lastro.core.domain.model

import java.math.BigDecimal

data class User(
    val id: String,
    val name: String,
    val email: String,
    val monthlyIncome: BigDecimal?,
    val savingGoalPct: Double?,
    val coupleId: String?,
    val partner: Partner?,
)

data class Partner(
    val id: String,
    val name: String,
    val email: String,
)
