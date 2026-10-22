package com.lastro.core.domain.model

data class Category(
    val id: String,
    val name: String,
    val type: BudgetType,
    val icon: String?,
    val color: String?,
)
