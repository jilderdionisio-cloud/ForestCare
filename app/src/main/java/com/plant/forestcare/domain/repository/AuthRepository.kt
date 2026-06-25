package com.plant.forestcare.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    fun getCurrentUserId(): String?
    suspend fun logout()
}
