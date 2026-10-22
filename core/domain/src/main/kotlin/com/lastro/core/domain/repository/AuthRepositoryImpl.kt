package com.lastro.core.domain.repository

import com.lastro.core.common.Result
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> =
        Result.Success(Unit)

    override suspend fun register(email: String, password: String, name: String): Result<Unit> =
        Result.Success(Unit)

    override suspend fun logout() = Unit

    override suspend fun isLoggedIn(): Boolean = false
}
