package com.tymeX.github

import androidx.lifecycle.SavedStateHandle
import com.tymeX.github.mapper.toUiState
import com.tymeX.github.ui.navigation.NAV_ARG_LOGIN_NAME
import com.tymeX.github.ui.screens.userdetails.UserDetailUiState
import com.tymeX.github.ui.screens.userdetails.UserDetailViewModel
import com.tymex.domain.model.UserDetail
import com.tymex.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDetailViewModelTest {
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = mockk()
    private lateinit var viewModel: UserDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { savedStateHandle.get<String>(NAV_ARG_LOGIN_NAME) } returns "testuser"
        viewModel = UserDetailViewModel(userRepository, savedStateHandle)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserDetail should update uiState to Loading initially`() = runTest {
        Assert.assertEquals(UserDetailUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `getUserDetail should update uiState to Success when repository returns success`() =
        runTest {
            val mockUserDetail = UserDetail(
                login = "testuser",
                avatarUrl = "avatar.url",
                htmlUrl = "html.url",
                location = "Location",
                followers = 100,
                following = 50,
            )
            coEvery { savedStateHandle.get<String>(NAV_ARG_LOGIN_NAME) } returns "testuser"
            coEvery { userRepository.getUserDetail("testuser") } returns flowOf(
                Result.success(
                    mockUserDetail
                )
            )

            viewModel.getUserDetail()

            val expectedUiState = UserDetailUiState.Success(mockUserDetail.toUiState())
            assertEquals(expectedUiState, viewModel.uiState.value)
        }

    @Test
    fun `getUserDetail should update uiState to Error when repository returns failure`() = runTest {
        val error = Exception("Network error")
        coEvery { userRepository.getUserDetail(any()) } returns flowOf(Result.failure(error))

        viewModel.getUserDetail()

        assertEquals(UserDetailUiState.Error, viewModel.uiState.value)
    }
}