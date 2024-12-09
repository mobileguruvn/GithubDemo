package com.tymex.network.di

import com.tymex.network.UsersRemoteDataSource
import com.tymex.network.UsersRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindUsersRemoteDataSource(impl: UsersRemoteDataSourceImpl): UsersRemoteDataSource

}

