package com.tymex.network

import android.util.Log
import com.tymex.network.model.UserDetailDto
import com.tymex.network.model.UserDto
import kotlinx.coroutines.delay
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

interface UsersRemoteDataSource {
    suspend fun fetchUsers(perPage: Int, since: Int): Result<List<UserDto>>
    suspend fun fetchUserDetail(loginUsername: String): Result<UserDetailDto>
}

class UsersRemoteDataSourceImpl @Inject constructor(private val gitHubApi: GitHubApi) :
    UsersRemoteDataSource {
    override suspend fun fetchUsers(
        perPage: Int,
        since: Int,
    ): Result<List<UserDto>> {
        return try {
            val response: Response<List<UserDto>> = gitHubApi.getUsers(perPage, since)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Log.e("UsersRemoteDataSource", "Error fetching users: $errorMessage")
                // Handle API error
                Result.failure(Exception(parseErrorMessage(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Log.e("UsersRemoteDataSource", "Error fetching users", e)
            Result.failure(e)
        }
    }

    override suspend fun fetchUserDetail(loginUsername: String): Result<UserDetailDto> {
        return try {
            val response: Response<UserDetailDto> =
                gitHubApi.getUserDetail(loginUsername = loginUsername)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception(parseErrorMessage(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseErrorMessage(errorBody: String?): String {
        Log.e("UsersRemoteDataSource", "Error body: $errorBody")
        if (errorBody.isNullOrEmpty()) {
            return "An error occurred while parsing the error message"
        }
        return try {
            val json = JSONObject(errorBody.trimIndent())
            json.getString("message")
        } catch (e: JSONException) {
            e.printStackTrace()
            "An error occurred while parsing the error message"
        }
    }
}