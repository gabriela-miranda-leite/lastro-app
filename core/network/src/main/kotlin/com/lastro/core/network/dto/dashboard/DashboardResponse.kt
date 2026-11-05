package com.lastro.core.network.dto.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("income") val income: String,
    @SerializedName("expenses") val expenses: ExpensesSummaryDto?,
    @SerializedName("savings") val savings: SavingsSummaryDto?,
    @SerializedName("couple") val couple: CoupleSummaryDto?,
)

data class ExpensesSummaryDto(
    @SerializedName("total") val total: String,
    @SerializedName("by_type") val byType: Map<String, TypeBreakdownDto>,
)

data class TypeBreakdownDto(
    @SerializedName("amount") val amount: String,
    @SerializedName("pct") val pct: String,
    @SerializedName("target_pct") val targetPct: String,
    @SerializedName("status") val status: String,
)

data class SavingsSummaryDto(
    @SerializedName("goal") val goal: String,
    @SerializedName("saved") val saved: String,
    @SerializedName("status") val status: String,
)

data class CoupleSummaryDto(
    @SerializedName("income") val income: String,
    @SerializedName("expenses") val expenses: ExpensesSummaryDto?,
)
