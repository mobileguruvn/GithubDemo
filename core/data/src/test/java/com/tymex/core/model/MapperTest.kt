package com.tymex.core.model

import com.tymex.core.data.mapper.toDomainModel
import com.tymex.core.data.mapper.toEntity
import com.tymex.database.model.UserEntity
import com.tymex.network.model.UserDetailDto
import com.tymex.network.model.UserDto
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun userDtoMapsToEntity() {
        val userDto = UserDto(
            id = 1,
            login = "user1",
            avatarUrl = "avatar1",
            htmlUrl = "html1"
        )

        val userEntity = userDto.toEntity()

        assertEquals(userDto.id, userEntity.id)
        assertEquals(userDto.login, userEntity.login)
        assertEquals(userDto.avatarUrl, userEntity.avatarUrl)
        assertEquals(userDto.htmlUrl, userEntity.htmlUrl)
    }

    @Test
    fun userEntityMapToDomainModel() {
        val userEntity = UserEntity(
            id = 1,
            login = "user1",
            avatarUrl = "avatar1",
            htmlUrl = "html1"
        )

        val user = userEntity.toDomainModel()
        assertEquals(userEntity.id, user.id)
        assertEquals(userEntity.login, user.login)
        assertEquals(userEntity.avatarUrl, user.avatarUrl)
        assertEquals(userEntity.htmlUrl, user.htmlUrl)
    }

    @Test
    fun userDetailDtoMapsToEntity() {
        val userDetailDto = UserDetailDto(
            id = 1,
            location = "Vietnam",
            followers = 10,
            following = 20
        )
        val userDetailEntity = userDetailDto.toEntity()
        assertEquals(userDetailDto.id, userDetailEntity.id)
        assertEquals(userDetailDto.location, userDetailEntity.location)
        assertEquals(userDetailDto.followers, userDetailEntity.followers)
    }
}