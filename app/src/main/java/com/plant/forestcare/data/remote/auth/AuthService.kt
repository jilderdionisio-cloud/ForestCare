package com.plant.forestcare.data.remote.auth

import kotlinx.coroutines.delay

interface AuthService {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    fun getCurrentUserId(): String?
    suspend fun logout()
}

class AuthServiceImpl : AuthService {
    private var currentUserId: String? = null

    override suspend fun login(email: String, password: String): Result<String> {
        // Mock delay for network simulation
        delay(1000)
        return if (email.contains("@") && password.length >= 6) {
            currentUserId = "mock_uid_${email.hashCode()}"
            Result.success(currentUserId!!)
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<String> {
        delay(1500)
        return if (name.isNotBlank() && email.contains("@") && password.length >= 6) {
            currentUserId = "mock_uid_${email.hashCode()}"
            Result.success(currentUserId!!)
        } else {
            Result.failure(Exception("Datos de registro inválidos"))
        }
    }

    override fun getCurrentUserId(): String? = currentUserId

    override suspend fun logout() {
        delay(500)
        currentUserId = null
    }
}
