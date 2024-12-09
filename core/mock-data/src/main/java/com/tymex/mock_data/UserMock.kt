package com.tymex.mock_data

import com.tymex.database.model.UserEntity


val usersEntityMock = listOf(
    UserEntity(
        id = 1,
        login = "jvantuyl",
        avatarUrl = "https://avatars.githubusercontent.com/u/101?v=4",
        htmlUrl = "https://github.com/jvantuyl"
    ),
    UserEntity(
        id = 2,
        login = "BrianTheCoder",
        avatarUrl = "https://avatars.githubusercontent.com/u/102?v=4",
        htmlUrl = "https://github.com/BrianTheCoder"
    ),
    UserEntity(
        id = 3,
        login = "freeformz",
        avatarUrl = "https://avatars.githubusercontent.com/u/103?v=4",
        htmlUrl = "https://github.com/freeformz"
    )
)
