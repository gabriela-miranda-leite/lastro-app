package com.lastro.core.testing.repository

import com.lastro.core.common.Result
import com.lastro.core.domain.repository.AuthRepository
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    var loginResult: Result<Unit> = Result.Success(Unit)
    var registerResult: Result<Unit> = Result.Success(Unit)
    var forgotPasswordResult: Result<Unit> = Result.Success(Unit)
    var resetPasswordResult: Result<Unit> = Result.Success(Unit)
    var refreshResult: Result<Unit> = Result.Success(Unit)
    var loggedIn: Boolean = false
    var logoutAllCalled: Boolean = false

    override suspend fun login(email: String, password: String): Result<Unit> = loginResult

    override suspend fun register(email: String, password: String, name: String): Result<Unit> =
        registerResult

    override suspend fun logout() {
        loggedIn = false
    }

    override suspend fun isLoggedIn(): Boolean = loggedIn

    override suspend fun forgotPassword(email: String): Result<Unit> = forgotPasswordResult

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> =
        resetPasswordResult

    override suspend fun refresh(): Result<Unit> = refreshResult

    override suspend fun logoutAll() {
        logoutAllCalled = true
        loggedIn = false
    }
}
