package com.plant.forestcare.domain.auth

import com.plant.forestcare.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<String> {
        return repository.register(name, email, password)
    }
}
