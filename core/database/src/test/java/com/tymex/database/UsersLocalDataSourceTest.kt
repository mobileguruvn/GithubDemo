package com.tymex.database

import androidx.paging.PagingSource
import com.tymex.database.dao.UserDao
import com.tymex.database.mock.usersEntityMock
import com.tymex.database.model.UserDetailEntity
import com.tymex.database.model.UserEntity
import com.tymex.database.model.UserWithDetailEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UsersLocalDataSourceTest {

    private val userDao: UserDao = mockk()
    private lateinit var usersLocalDataSource: UsersLocalDataSource

    @Before
    fun setup() {
        usersLocalDataSource = UsersLocalDataSourceImpl(userDao)
    }

    @Test
    fun `getUsers return PagingSource with userEntity from UserDao`() {
        val pagingSource: PagingSource<Int, UserEntity> = mockk()
        every { userDao.getUsers() } returns pagingSource

        val actualResult = usersLocalDataSource.getUsers()

        assertEquals(pagingSource, actualResult)
        verify(exactly = 1) { userDao.getUsers() }

    }

    @Test
    fun `insertUsers calls with correct parameters from UserDao`() = runBlocking {

        coEvery { userDao.insertUsers(usersEntityMock) } just Runs
        usersLocalDataSource.insertUsers(usersEntityMock)

        coVerify(exactly = 1) { userDao.insertUsers(usersEntityMock) }


    }

    @Test
    fun `insertUserDetail calls insertUserDetail from UserDao`() = runBlocking {
        val userDetail = UserDetailEntity(
            id = 1,
            location = "Vietnam",
            followers = 10,
            following = 20
        )

        coEvery { userDao.insertUserDetail(userDetail) } just Runs
        usersLocalDataSource.insertUserDetail(userDetail)

        coVerify(exactly = 1) { userDao.insertUserDetail(userDetail) }
    }

    @Test
    fun `getUserWithDetail should return Flow from UserDao`() = runTest {
        val mockUserWithDetailEntity = UserWithDetailEntity(
            user = UserEntity(
                id = 1,
                login = "user1",
                avatarUrl = "avatar1",
                htmlUrl = "htmlUrl1"
            ),
            userDetail = UserDetailEntity(
                id = 1,
                location = "Vietnam",
                followers = 10,
                following = 20
            )
        )
        every { userDao.getUserWithDetail(any()) } returns flowOf(mockUserWithDetailEntity)

        val result = usersLocalDataSource.getUserWithDetail("1")

        result.collect { userWithDetailEntity ->
            assertEquals(mockUserWithDetailEntity, userWithDetailEntity)
        }

        verify(exactly = 1) { userDao.getUserWithDetail("1") }
    }

    @Test
    fun `clearUsers calls UserDao`() = runBlocking {
        coEvery { userDao.clearUsers() } just Runs
        usersLocalDataSource.clearUsers()
        coVerify(exactly = 1) { userDao.clearUsers() }
    }
}