package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.model.BudgetType
import com.lastro.core.domain.model.Category
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor() : CategoryRepository {

    override suspend fun getCategories(): Result<List<Category>> =
        Result.Success(emptyList())

    override suspend fun createCategory(name: String, type: BudgetType, icon: String?, color: String?): Result<Category> =
        Result.Error(NotImplementedError())

    override suspend fun updateCategory(id: String, name: String?, type: BudgetType?, icon: String?, color: String?): Result<Category> =
        Result.Error(NotImplementedError())

    override suspend fun deleteCategory(id: String): Result<Unit> =
        Result.Error(NotImplementedError())
}
