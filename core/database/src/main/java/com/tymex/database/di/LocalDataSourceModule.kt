package com.tymex.database.di

import com.tymex.database.UsersLocalDataSource
import com.tymex.database.UsersLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindUsersLocalDataSource(impl: UsersLocalDataSourceImpl): UsersLocalDataSource

}

