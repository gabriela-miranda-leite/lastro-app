package com.lastro.core.network.dto.user

import com.google.gson.annotations.SerializedName

data class ExportDataDto(
    @SerializedName("user") val user: ExportUserDto,
    @SerializedName("transactions") val transactions: List<ExportTransactionDto>,
    @SerializedName("recurring_expenses") val recurringExpenses: List<ExportRecurringExpenseDto>,
    @SerializedName("monthly_savings") val monthlySavings: List<ExportMonthlySavingDto>,
    @SerializedName("audit_log") val auditLog: List<ExportAuditEntryDto>,
)

data class ExportUserDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("created_at") val createdAt: String,
)

data class ExportTransactionDto(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("description") val description: String?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("is_shared") val isShared: Boolean,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("date") val date: String,
    @SerializedName("created_at") val createdAt: String,
)

data class ExportRecurringExpenseDto(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("due_day") val dueDay: Int,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("active") val active: Boolean,
    @SerializedName("created_at") val createdAt: String,
)

data class ExportMonthlySavingDto(
    @SerializedName("id") val id: String,
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("goal_amount") val goalAmount: String,
    @SerializedName("saved_amount") val savedAmount: String,
)

data class ExportAuditEntryDto(
    @SerializedName("id") val id: String,
    @SerializedName("action") val action: String,
    @SerializedName("created_at") val createdAt: String,
)
