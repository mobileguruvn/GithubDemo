package com.tymeX.github.ui.navigation

const val NAV_ARG_LOGIN_NAME = "login_name"

sealed class Screen(val route: String) {
    object UserList : Screen("userList")

    object UserDetail : Screen("userDetail/{$NAV_ARG_LOGIN_NAME}") {
        fun createRoute(loginName: String) = "userDetail/$loginName"
    }

}
