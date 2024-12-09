package com.tymex.testing

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher


@Module
@InstallIn(SingletonComponent::class)
object TestDispatcherModule {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun providesTestDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

}