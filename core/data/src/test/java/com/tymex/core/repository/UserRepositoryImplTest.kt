package com.tymex.core.repository

import com.tymex.core.data.mapper.toDomainModel
import com.tymex.core.data.mapper.toEntity
import com.tymex.core.data.repository.UserRepositoryImpl
import com.tymex.core.data.repository.UsersRemoteMediator
import com.tymex.database.GitHubUserDatabase
import com.tymex.database.UsersLocalDataSource
import com.tymex.database.model.UserEntity
import com.tymex.database.model.UserWithDetailEntity
import com.tymex.domain.repository.UserRepository
import com.tymex.network.UsersRemoteDataSource
import com.tymex.network.model.UserDetailDto
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {
    private val remoteDataSource = mockk<UsersRemoteDataSource>()
    private val localDataSource = mockk<UsersLocalDataSource>()
    private val usersRemoteMediator = mockk<UsersRemoteMediator>()
    private val remoteMediator = mockk<UsersRemoteDataSource>()
    private val gitHubUserDatabase = mockk<GitHubUserDatabase>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ioDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: UserRepository


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(
            remoteDataSource,
            localDataSource,
            usersRemoteMediator,
            ioDispatcher
        )
    }

    @Test
    fun `getUserDetail should emit success when remote call success and local data is available`() =
        runTest {

            // Given
            val userDetailDto = UserDetailDto(
                id = 1,
                location = "Location",
                followers = 100,
                following = 50
            )
            val userEntity = UserEntity(
                id = 1,
                login = "testuser",
                avatarUrl = "avatarUrl",
                htmlUrl = "htmlUrl"
            )
            val userDetailEntity = userDetailDto.toEntity()
            val userWithDetailEntity = UserWithDetailEntity(
                user = userEntity,
                userDetail = userDetailEntity
            )
            val userDetailDomain = userWithDetailEntity.toDomainModel()
            val remoteResponse = Result.success(userDetailDto)

            coEvery { remoteDataSource.fetchUserDetail("testuser") } returns remoteResponse
            coEvery { localDataSource.insertUserDetail(userDetailEntity) } just Runs
            coEvery { localDataSource.getUserWithDetail("1") } returns flowOf(userWithDetailEntity)

            // When
            val result = repository.getUserDetail("testuser")

            // Then
            assert(result.first().isSuccess)
            assertEquals(userDetailDomain, result.first().getOrNull())
            coVerify { remoteDataSource.fetchUserDetail("testuser") }
            coVerify { localDataSource.insertUserDetail(userDetailEntity) }
            coVerify { localDataSource.getUserWithDetail("1") }
        }

    @Test
    fun `getUserDetail should emit failure when remote call fails`() = runTest {
        // Given
        val error = Exception("Network error")
        val remoteResponse = Result.failure<UserDetailDto>(error)

        coEvery { remoteDataSource.fetchUserDetail("testuser") } returns remoteResponse

        // When
        val result = repository.getUserDetail("testuser")

        // Then
        assert(result.first().isFailure)
        assertEquals(error, result.first().exceptionOrNull())
        coVerify { remoteDataSource.fetchUserDetail("testuser") }
        coVerify(exactly = 0) { localDataSource.insertUserDetail(any()) }
        coVerify(exactly = 0) { localDataSource.getUserWithDetail(any()) }
    }
}