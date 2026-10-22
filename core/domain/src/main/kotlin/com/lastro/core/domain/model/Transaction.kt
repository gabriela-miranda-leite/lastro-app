package com.lastro.core.domain.model

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: Long,
    val type: TransactionType,
)

enum class TransactionType {
    INCOME,
    EXPENSE,
}
