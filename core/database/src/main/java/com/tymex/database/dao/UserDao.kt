package com.tymex.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tymex.database.model.UserDetailEntity
import com.tymex.database.model.UserEntity
import com.tymex.database.model.UserWithDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): PagingSource<Int, UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userDetail: UserDetailEntity)

    @Transaction
    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserWithDetail(userId: String): Flow<UserWithDetailEntity>

    @Query("DELETE FROM user")
    suspend fun clearUsers()
}