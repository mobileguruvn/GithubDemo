package com.tymex.core.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tymex.database.model.UserEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FakeUserRemoteMediatorSuccessImpl @Inject constructor() : RemoteMediator<Int, UserEntity>() {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>,
    ): MediatorResult {
        return MediatorResult.Success(endOfPaginationReached = true)
    }
}