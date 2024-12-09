package com.tymex.domain.repository

import androidx.paging.PagingData
import com.tymex.domain.model.User
import com.tymex.domain.model.UserDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<PagingData<User>>

    fun getUserDetail(loginUsername: String): Flow<Result<UserDetail>>
}