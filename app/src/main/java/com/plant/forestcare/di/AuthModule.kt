package com.plant.forestcare.di

import com.plant.forestcare.data.remote.auth.AuthService
import com.plant.forestcare.data.remote.auth.AuthServiceImpl
import com.plant.forestcare.data.repository.AuthRepositoryImpl
import com.plant.forestcare.domain.auth.LoginUseCase
import com.plant.forestcare.domain.auth.RegisterUseCase
import com.plant.forestcare.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(authService)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }
}
