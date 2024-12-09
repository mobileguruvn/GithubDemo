package com.tymex.database

import androidx.paging.PagingSource
import com.tymex.database.dao.UserDao
import com.tymex.database.model.UserDetailEntity
import com.tymex.database.model.UserEntity
import com.tymex.database.model.UserWithDetailEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UsersLocalDataSource {
    fun getUsers(): PagingSource<Int, UserEntity>
    suspend fun insertUsers(users: List<UserEntity>)
    suspend fun insertUserDetail(userDetail: UserDetailEntity)
    fun getUserWithDetail(id: String): Flow<UserWithDetailEntity>
    suspend fun clearUsers()
}

class UsersLocalDataSourceImpl @Inject constructor(private val userDao: UserDao) :
    UsersLocalDataSource {

    override fun getUsers(): PagingSource<Int, UserEntity> {
        return userDao.getUsers()
    }

    override suspend fun insertUsers(users: List<UserEntity>) {
        return userDao.insertUsers(users)
    }

    override suspend fun insertUserDetail(userDetail: UserDetailEntity) {
        return userDao.insertUserDetail(userDetail)
    }

    override fun getUserWithDetail(id: String): Flow<UserWithDetailEntity> {
        return userDao.getUserWithDetail(id)
    }

    override suspend fun clearUsers() {
        userDao.clearUsers()
    }
}