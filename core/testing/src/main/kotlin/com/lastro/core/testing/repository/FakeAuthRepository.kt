package com.lastro.core.testing.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    var loginResult: Result<Unit> = Result.Success(Unit)
    var registerResult: Result<Unit> = Result.Success(Unit)
    var loggedIn: Boolean = false

    override suspend fun login(email: String, password: String): Result<Unit> = loginResult

    override suspend fun register(email: String, password: String, name: String): Result<Unit> =
        registerResult

    override suspend fun logout() {
        loggedIn = false
    }

    override suspend fun isLoggedIn(): Boolean = loggedIn
}
