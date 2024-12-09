package com.tymex.core.data.di

import com.tymex.core.data.repository.UserRepositoryImpl
import com.tymex.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindGitHubRepository(impl: UserRepositoryImpl): UserRepository

}