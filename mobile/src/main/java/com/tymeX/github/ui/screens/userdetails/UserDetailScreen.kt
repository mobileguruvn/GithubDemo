package com.tymeX.github.ui.screens.userdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tymeX.github.R
import com.tymeX.github.ui.theme.GitHubTheme
import com.tymeX.github.utils.formatNumberByStep
import com.tymeX.github.utils.safe
import com.tymex.core.designsystem.component.CircleAvatarImage
import com.tymex.core.designsystem.component.FollowState

@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is UserDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UserDetailUiState.Error -> {
            Text("Error: Unknown error", color = MaterialTheme.colorScheme.error)
        }

        is UserDetailUiState.Success -> {
            val userDetail = (uiState as UserDetailUiState.Success).userWithDetail
            UserDetailContent(
                uiState = userDetail,
                modifier,
                contentPadding
            )
        }
    }

}

@Composable
fun UserDetailContent(
    uiState: UserDetailItemUi,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier = modifier.padding(contentPadding)) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_3dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_12dp))
                    .sizeIn(minHeight = dimensionResource(R.dimen.card_item_height)),
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
                    LocationText(uiState.location ?: "NA")
                }

            }
        }

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            FollowState(
                icon = Icons.Default.People,
                statCount = formatNumberByStep(number = uiState.followers.safe()),
                statTitle = stringResource(R.string.label_follower)
            )
            FollowState(
                icon = Icons.Filled.SportsGolf,
                statCount = formatNumberByStep(number = uiState.following.safe()),
                statTitle = stringResource(R.string.label_following)
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(R.string.text_blog),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = uiState.htmlUrl,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun LocationText(location: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = location,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailScreenPreview() {
    GitHubTheme {
        UserDetailContent(
            uiState = UserDetailItemUi(
                login = "abc",
                avatarUrl = "",
                followers = 10,
                htmlUrl = "google.com",
                following = 10
            )
        )
    }
}