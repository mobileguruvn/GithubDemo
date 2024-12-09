package com.tymeX.github.ui.screens.users

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tymeX.github.R
import com.tymeX.github.ui.theme.GitHubTheme
import com.tymex.core.designsystem.component.CircleAvatarImage
import com.tymex.core.designsystem.component.UnderlineTextClickable


@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val usersPaging = viewModel.usersPaging.collectAsLazyPagingItems()

    // First time when no data: Loading or Error states
    val loadState = usersPaging.loadState.refresh
    when {
        loadState is LoadState.Loading -> {
            Log.d("UserListScreen", "Loading")
            InitialLoading(modifier = modifier)
        }
        loadState is LoadState.Error -> {
            Log.d("UserListScreen", "Error")
            val error = (usersPaging.loadState.refresh as LoadState.Error).error
            InitialLoadingError(
                modifier = modifier,
                errorMessage = error.message ?: "Unknown error"
            )
        }
        usersPaging.itemCount > 0 -> { // PagingData loaded successfully
            UserListSuccess(
                usersPaging = usersPaging,
                onUserClick = onUserClick,
                modifier = modifier,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
fun UserListSuccess(
    modifier: Modifier = Modifier,
    usersPaging: LazyPagingItems<UserUiState>,
    onUserClick: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    Log.d("UserListScreen", "usersPaging: ${usersPaging.itemCount}")
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(usersPaging.itemCount) { index ->
            usersPaging[index]?.let { user ->
                UserCard(uiState = user, onUserClick = {
                    onUserClick(it)
                }, modifier = Modifier.fillMaxWidth())
            }
        }
        // Loading more states: loading or error
        usersPaging.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    Log.d("UserListScreen", "Loading more items")
                    item { LoadingMoreIndicator() }
                }

                loadState.append is LoadState.Error || loadState.refresh is LoadState.Error -> {
                    item { LoadingMoreError() }
                }
            }
        }
    }
}

@Composable
fun UserCard(
    uiState: UserUiState,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_3dp)),
        modifier = modifier
            .clickable { onUserClick(uiState.login) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_12dp))
                .sizeIn(minHeight = dimensionResource(R.dimen.card_item_height)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleAvatarImage(avatarUrl = uiState.avatarUrl)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensionResource(R.dimen.padding_12dp)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = uiState.login.capitalize(Locale.current),
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = dimensionResource(R.dimen.thickness_divider)
                )
                UnderlineTextClickable(linkText = uiState.htmlUrl)
            }

        }
    }
}

@Composable
private fun InitialLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
    }
}

@Composable
private fun InitialLoadingError(errorMessage: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Failed to load data: $errorMessage",
            color = Color.Red,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun LoadingMoreIndicator() {
    Box(modifier = Modifier.fillMaxSize()) {
        LinearProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun LoadingMoreError() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Error loading more items",
            color = Color.Red,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    GitHubTheme {
        UserCard(uiState = UserUiState(
            login = "mojombo",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            htmlUrl = "https://github.com/mojombo"
        ), onUserClick = {})
    }
}