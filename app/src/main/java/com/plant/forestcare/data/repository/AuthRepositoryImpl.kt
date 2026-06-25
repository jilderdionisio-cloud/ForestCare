package com.plant.forestcare.data.repository

import com.plant.forestcare.data.remote.auth.AuthService
import com.plant.forestcare.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<String> {
        return authService.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): Result<String> {
        return authService.register(name, email, password)
    }

    override fun getCurrentUserId(): String? {
        return authService.getCurrentUserId()
    }

    override suspend fun logout() {
        authService.logout()
    }
}
