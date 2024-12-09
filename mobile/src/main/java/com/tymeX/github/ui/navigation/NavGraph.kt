package com.tymeX.github.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tymeX.github.ui.navigation.Screen.UserDetail
import com.tymeX.github.ui.screens.userdetails.UserDetailScreen
import com.tymeX.github.ui.screens.users.UserListScreen

@Composable
fun GithubNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.UserList.route
    ) {
        composable(route = Screen.UserList.route) {
            UserListScreen(onUserClick = { loginName ->
                navController.navigate(UserDetail.createRoute(loginName))
            }, modifier = modifier, contentPadding = contentPadding)
        }

        composable(route = UserDetail.route,
            arguments = listOf(
                navArgument(NAV_ARG_LOGIN_NAME) { type = NavType.StringType }
            )) { backStackEntry ->
            UserDetailScreen(modifier = modifier, contentPadding = contentPadding)
        }
    }
}