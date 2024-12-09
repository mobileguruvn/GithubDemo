package com.tymex.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tymex.common.di.Dispatcher
import com.tymex.common.di.GithubDispatcher
import com.tymex.core.data.mapper.toDomainModel
import com.tymex.core.data.mapper.toEntity
import com.tymex.database.UsersLocalDataSource
import com.tymex.domain.model.User
import com.tymex.domain.model.UserDetail
import com.tymex.domain.repository.UserRepository
import com.tymex.network.UsersRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 20

class UserRepositoryImpl @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteMediator: UsersRemoteMediator,
    @Dispatcher(GithubDispatcher.IO)
    private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
            ),
            remoteMediator = usersRemoteMediator,
            pagingSourceFactory = {
                usersLocalDataSource.getUsers()
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomainModel()
            }
        }.flowOn(ioDispatcher)
    }


    override fun getUserDetail(loginUsername: String): Flow<Result<UserDetail>> {
        return flow {
            try {
                val response =
                    usersRemoteDataSource.fetchUserDetail(loginUsername)
                when {
                    response.isSuccess -> {
                        val userDetailDto = response.getOrNull()
                        if (userDetailDto != null) {
                            // Save user detail to database
                            usersLocalDataSource.insertUserDetail(userDetailDto.toEntity())
                            // Emit success result
                            emitAll(usersLocalDataSource.getUserWithDetail(id = userDetailDto.id.toString())
                                    .map {
                                        Result.success(it.toDomainModel())
                                    })
                        } else {
                            emit(Result.failure(Exception("User detail is null")))
                        }
                    }

                    else -> {
                        emit(Result.failure(response.exceptionOrNull() ?: Exception()))
                    }
                }
            } catch (ex: Exception) {
                emit(Result.failure(ex))
            }

        }.flowOn(ioDispatcher)

    }
}