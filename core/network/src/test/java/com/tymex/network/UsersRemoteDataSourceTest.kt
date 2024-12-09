package com.tymex.network

import android.util.Log
import com.tymex.network.model.UserDetailDto
import com.tymex.network.model.UserDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertIs

class UsersRemoteDataSourceTest {
    private val gitHubApi: GitHubApi = mockk()

    private lateinit var usersRemoteDataSource: UsersRemoteDataSource

    @Before
    fun setup() {
        usersRemoteDataSource = UsersRemoteDataSourceImpl(gitHubApi)
    }

    @Test
    fun `fetchUsers returns success when API call is successful`() = runTest {
        val perPage = 10
        val since = 0
        val userDtos = listOf<UserDto>(
            UserDto(
                login = "user1",
                id = 1,
                avatarUrl = "avatar1",
                htmlUrl = "htmlUrl1"
            ),
            UserDto(
                login = "user2",
                id = 2,
                avatarUrl = "avatar2",
                htmlUrl = "htmlUrl2"
            ),
            UserDto(
                login = "user3",
                id = 3,
                avatarUrl = "avatar3",
                htmlUrl = "htmlUrl3"
            )
        )
        val response = Response.success(userDtos)
        coEvery { gitHubApi.getUsers(any(), any()) } returns response

        val actualResult = usersRemoteDataSource.fetchUsers(perPage, since)

        val expectedResult = Result.success(userDtos)
        assertTrue(actualResult.isSuccess)
        assertTrue(userDtos == actualResult.getOrNull())
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `fetchUsers returns failure when API call fails`() = runTest {
        val perPage = 10
        val since = 0
        val error = IOException("Network error")
        coEvery { gitHubApi.getUsers(any(), any()) } throws error

        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        val actualResult = usersRemoteDataSource.fetchUsers(perPage, since)

        assertTrue(actualResult.isFailure)
        assertIs<IOException>(actualResult.exceptionOrNull())
    }

    @Test
    fun `fetchUsers logs when API call fails`() = runTest {
        val error = Exception("Exception error")
        coEvery { gitHubApi.getUsers(any(), any()) } throws error

        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        val result = usersRemoteDataSource.fetchUsers(perPage = 10, since = 0)

        assertTrue(result.isFailure)
        verify { Log.e("UsersRemoteDataSource", "Error fetching users", error) }
    }

    @Test
    fun `fetchUsers returns failure when body null`() = runTest {
        val perPage = 10
        val since = 0
        val response = Response.success<List<UserDto>>(null)

        coEvery { gitHubApi.getUsers(any(), any()) } returns response

        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        val actualResult = usersRemoteDataSource.fetchUsers(perPage, since)

        assertTrue(actualResult.isFailure)
        assertIs<Exception>(actualResult.exceptionOrNull())

    }

    @Test
    fun `fetchUserDetail returns success when API call is successful`() = runTest {
        val loginUsername = "user1"

        val userDetailDto = UserDetailDto(
            id = 1,
            location = "VN",
            followers = 100,
            following = 1
        )

        val response = Response.success(userDetailDto)
        coEvery { gitHubApi.getUserDetail(any()) } returns response

        val actualResult = usersRemoteDataSource.fetchUserDetail(loginUsername)

        val expectedResult = Result.success(userDetailDto)
        assertTrue(actualResult.isSuccess)
        assertTrue(userDetailDto == actualResult.getOrNull())
        assertEquals(expectedResult, actualResult)

    }

    @Test
    fun `fetchUserDetail returns failure when API call fails`() = runTest {
        val loginUsername = "user1"
        val error = IOException("Network error")

        coEvery { gitHubApi.getUserDetail(any()) } throws error

        val actualResult = usersRemoteDataSource.fetchUserDetail(loginUsername)

        assertTrue(actualResult.isFailure)
        assertIs<IOException>(actualResult.exceptionOrNull())
    }

    @Test
    fun `fetchUserDetail returns failure when body null`() = runTest {
        val loginUsername = "user1"
        val response = Response.success<UserDetailDto>(null)
        coEvery { gitHubApi.getUserDetail(any()) } returns response

        val actualResult = usersRemoteDataSource.fetchUserDetail(loginUsername)

        assertTrue(actualResult.isFailure)
        assertIs<Exception>(actualResult.exceptionOrNull())
    }
}