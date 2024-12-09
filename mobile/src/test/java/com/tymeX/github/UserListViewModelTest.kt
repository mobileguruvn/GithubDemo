package com.tymeX.github

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.tymeX.github.ui.screens.users.UserListViewModel
import com.tymeX.github.ui.screens.users.UserUiState
import com.tymex.domain.model.User
import com.tymex.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class UserListViewModelTest {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private lateinit var viewModel: UserListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = UserListViewModel(userRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `usersPaging should emit PagingData when getUsers is called`() = runBlocking {

        val pagingData: PagingData<User> = PagingData.from(mockUsers)
        every { userRepository.getUsers() } returns flowOf(pagingData)

        val mockUserUiState = listOf(
            UserUiState(
                login = "jvantuyl",
                avatarUrl = "https://avatars.githubusercontent.com/u/101?v=4",
                htmlUrl = "https://github.com/jvantuyl"
            ),
            UserUiState(
                login = "BrianTheCoder",
                avatarUrl = "https://avatars.githubusercontent.com/u/102?v=4",
                htmlUrl = "https://github.com/BrianTheCoder"
            ),
            UserUiState(
                login = "freeformz",
                avatarUrl = "https://avatars.githubusercontent.com/u/103?v=4",
                htmlUrl = "https://github.com/freeformz"
            )
        )
        val result = viewModel.usersPaging.asSnapshot()

        assertEquals(mockUsers.size, result.size)
        assertEquals(mockUserUiState[0], result[0])
        assertEquals(mockUserUiState[1], result[1])
    }
}

class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }

}

val mockUsers = listOf<User>(
    User(
        id = 1,
        login = "jvantuyl",
        avatarUrl = "https://avatars.githubusercontent.com/u/101?v=4",
        htmlUrl = "https://github.com/jvantuyl"
    ),
    User(
        id = 2,
        login = "BrianTheCoder",
        avatarUrl = "https://avatars.githubusercontent.com/u/102?v=4",
        htmlUrl = "https://github.com/BrianTheCoder"
    ),
    User(
        id = 3,
        login = "freeformz",
        avatarUrl = "https://avatars.githubusercontent.com/u/103?v=4",
        htmlUrl = "https://github.com/freeformz"
    )

)