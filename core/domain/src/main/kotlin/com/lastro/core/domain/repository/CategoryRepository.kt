package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.Category
import com.lastro.core.domain.model.BudgetType

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun createCategory(name: String, type: BudgetType, icon: String?, color: String?): Result<Category>
    suspend fun updateCategory(id: String, name: String?, type: BudgetType?, icon: String?, color: String?): Result<Category>
    suspend fun deleteCategory(id: String): Result<Unit>
}
