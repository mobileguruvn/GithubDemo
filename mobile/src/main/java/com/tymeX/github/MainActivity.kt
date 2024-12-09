package com.tymeX.github

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tymeX.github.ui.navigation.GithubNavGraph
import com.tymeX.github.ui.navigation.Screen
import com.tymeX.github.ui.theme.GitHubTheme
import com.tymex.core.designsystem.component.CenterTitleTopAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GithubApp()
                }
            }
        }
    }


    @Composable
    fun GithubApp() {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val canNavigateBack = navController.previousBackStackEntry != null
        val currentScreen = currentBackStackEntry?.destination?.route
        val screenTitle = when (currentScreen) {
            Screen.UserList.route -> R.string.user_list_title
            Screen.UserDetail.route -> R.string.user_detail_title
            else -> R.string.app_name
        }

        Scaffold(modifier = Modifier
            .fillMaxSize(),
            topBar = {
                CenterTitleTopAppBar(
                    screenTitle = screenTitle,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->
            GithubNavGraph(
                navController, modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp
                ), innerPadding
            )
        }
    }

}
