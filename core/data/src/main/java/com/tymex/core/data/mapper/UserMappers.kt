package com.tymex.core.data.mapper

import com.tymex.database.model.UserDetailEntity
import com.tymex.database.model.UserEntity
import com.tymex.database.model.UserWithDetailEntity
import com.tymex.domain.model.User
import com.tymex.domain.model.UserDetail
import com.tymex.network.model.UserDetailDto
import com.tymex.network.model.UserDto


fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        login = this.login,
        avatarUrl = this.avatarUrl,
        htmlUrl = this.htmlUrl
    )
}

fun UserEntity.toDomainModel(): User {
    return User(
        id = this.id,
        login = this.login,
        avatarUrl = this.avatarUrl,
        htmlUrl = this.htmlUrl
    )
}

fun UserDetailDto.toEntity(): UserDetailEntity {
    return UserDetailEntity(
        id = this.id,
        location = this.location,
        followers = this.followers,
        following = this.following
    )
}

fun UserWithDetailEntity.toDomainModel(): UserDetail {
    return UserDetail(
        login = this.user.login,
        avatarUrl = this.user.avatarUrl,
        htmlUrl = this.user.htmlUrl,
        location = this.userDetail?.location,
        followers = this.userDetail?.followers ?: 0,
        following = this.userDetail?.following ?: 0
    )
}