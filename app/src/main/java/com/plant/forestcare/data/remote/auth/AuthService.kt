package com.plant.forestcare.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

interface AuthService {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    fun getCurrentUserId(): String?
    suspend fun logout()
}

class AuthServiceImpl : AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Usuario no encontrado")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Error al crear usuario")
            
            // Update profile name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()
            
            Result.success(user.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    override suspend fun logout() {
        auth.signOut()
    }
}
