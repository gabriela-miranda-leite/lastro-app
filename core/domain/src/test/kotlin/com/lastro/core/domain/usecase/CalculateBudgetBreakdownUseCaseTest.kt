package com.lastro.core.domain.usecase

import com.lastro.core.domain.model.BudgetStatus
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.domain.model.Category
import com.lastro.core.domain.model.SyncStatus
import com.lastro.core.domain.model.Transaction
import com.lastro.core.domain.model.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class CalculateBudgetBreakdownUseCaseTest {

    private val useCase = CalculateBudgetBreakdownUseCase()

    @Test
    fun `returns empty list when income is zero`() {
        val result = useCase(BigDecimal.ZERO, emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns empty list when income is negative`() {
        val result = useCase(BigDecimal("-100"), emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `calculates UNDER status when spending is below target`() {
        val transactions = listOf(
            expense(amount = "200", type = BudgetType.NEEDS),
        )
        val result = useCase(BigDecimal("1000"), transactions)

        val needs = result.first { it.type == BudgetType.NEEDS }
        assertEquals(BigDecimal("200"), needs.amount)
        assertEquals(BudgetStatus.UNDER, needs.status)
    }

    @Test
    fun `calculates MET status when spending equals target`() {
        val transactions = listOf(
            expense(amount = "500", type = BudgetType.NEEDS),
        )
        val result = useCase(BigDecimal("1000"), transactions)

        val needs = result.first { it.type == BudgetType.NEEDS }
        assertEquals(BudgetStatus.MET, needs.status)
    }

    @Test
    fun `calculates OVER status when spending exceeds target`() {
        val transactions = listOf(
            expense(amount = "600", type = BudgetType.NEEDS),
        )
        val result = useCase(BigDecimal("1000"), transactions)

        val needs = result.first { it.type == BudgetType.NEEDS }
        assertEquals(BudgetStatus.OVER, needs.status)
    }

    @Test
    fun `ignores INCOME transactions`() {
        val transactions = listOf(
            income(amount = "999", type = BudgetType.NEEDS),
        )
        val result = useCase(BigDecimal("1000"), transactions)

        val needs = result.first { it.type == BudgetType.NEEDS }
        assertEquals(BigDecimal.ZERO, needs.amount)
    }

    @Test
    fun `ignores transactions without category`() {
        val transactions = listOf(
            expense(amount = "300", type = null),
        )
        val result = useCase(BigDecimal("1000"), transactions)

        result.forEach { assertEquals(BigDecimal.ZERO, it.amount) }
    }

    @Test
    fun `sums multiple transactions of the same type`() {
        val transactions = listOf(
            expense(amount = "100", type = BudgetType.WANTS),
            expense(amount = "150", type = BudgetType.WANTS),
        )
        val result = useCase(BigDecimal("1000"), transactions)

        val wants = result.first { it.type == BudgetType.WANTS }
        assertEquals(BigDecimal("250"), wants.amount)
    }

    @Test
    fun `returns breakdown for all three budget types`() {
        val result = useCase(BigDecimal("1000"), emptyList())
        assertEquals(3, result.size)
        assertTrue(result.any { it.type == BudgetType.NEEDS })
        assertTrue(result.any { it.type == BudgetType.WANTS })
        assertTrue(result.any { it.type == BudgetType.SAVINGS })
    }

    @Test
    fun `target percentages are 50-30-20`() {
        val result = useCase(BigDecimal("1000"), emptyList())
        assertEquals(BigDecimal("0.50"), result.first { it.type == BudgetType.NEEDS }.targetPercentage)
        assertEquals(BigDecimal("0.30"), result.first { it.type == BudgetType.WANTS }.targetPercentage)
        assertEquals(BigDecimal("0.20"), result.first { it.type == BudgetType.SAVINGS }.targetPercentage)
    }

    private fun expense(amount: String, type: BudgetType?) = transaction(amount, TransactionType.EXPENSE, type)
    private fun income(amount: String, type: BudgetType?) = transaction(amount, TransactionType.INCOME, type)

    private fun transaction(amount: String, direction: TransactionType, budgetType: BudgetType?) = Transaction(
        id = "id",
        clientId = "clientId",
        userId = "userId",
        amount = BigDecimal(amount),
        direction = direction,
        category = budgetType?.let { Category(id = "cat", name = "Cat", type = it, icon = null, color = null) },
        description = null,
        isShared = false,
        date = LocalDate.now(),
        syncStatus = SyncStatus.SYNCED,
    )
}
