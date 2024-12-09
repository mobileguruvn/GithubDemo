package com.tymeX.github.mapper

import com.tymeX.github.ui.screens.userdetails.UserDetailItemUi
import com.tymeX.github.ui.screens.users.UserUiState
import com.tymex.domain.model.User
import com.tymex.domain.model.UserDetail

fun User.toUiState() = UserUiState(
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
)

fun UserDetail.toUiState() = UserDetailItemUi(
    login = this.login,
    avatarUrl = this.avatarUrl,
    htmlUrl = this.htmlUrl,
    location = this.location,
    followers = this.followers,
    following = this.following
)