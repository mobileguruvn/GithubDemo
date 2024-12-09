package com.tymex.core.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tymex.database.model.UserEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FakeUserRemoteMediatorErrorImpl @Inject constructor() :
    RemoteMediator<Int, UserEntity>() {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>,
    ): MediatorResult {
        return try {
            // Simulating a network error
            throw Exception("Fake error")
        } catch (e: Exception) {
            Log.e("FakeRemoteMediator", "Error loading data", e)
            MediatorResult.Error(e)
        }
    }
}
