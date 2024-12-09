package com.tymex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tymex.database.dao.RemoteKeysDao
import com.tymex.database.dao.UserDao
import com.tymex.database.model.RemoteKeys
import com.tymex.database.model.UserDetailEntity
import com.tymex.database.model.UserEntity

@Database(
    entities = [UserEntity::class, UserDetailEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class GitHubUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}

// Migration missing