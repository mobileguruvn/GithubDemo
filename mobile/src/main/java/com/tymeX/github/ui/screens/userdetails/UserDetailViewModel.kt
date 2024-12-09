package com.tymeX.github.ui.screens.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tymeX.github.mapper.toUiState
import com.tymeX.github.ui.navigation.NAV_ARG_LOGIN_NAME
import com.tymeX.github.utils.safe
import com.tymex.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    private val loginName
        get() = savedStateHandle.get<String>(NAV_ARG_LOGIN_NAME).safe()

    init {
        getUserDetail()
    }

    fun getUserDetail() {
        _uiState.value = UserDetailUiState.Loading
        viewModelScope.launch {
            userRepository.getUserDetail(loginName)
                .onStart {
                    _uiState.value = UserDetailUiState.Loading
                }
                .collect { result ->
                    result.onSuccess { userDetail ->
                        _uiState.value = UserDetailUiState.Success(userDetail.toUiState())
                    }.onFailure {
                        _uiState.value = UserDetailUiState.Error
                    }
                }
        }
    }
}

sealed interface UserDetailUiState {
    data class Success(val userWithDetail: UserDetailItemUi) : UserDetailUiState
    object Error : UserDetailUiState
    object Loading : UserDetailUiState

}

data class UserDetailItemUi(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val location: String? = null,
    val followers: Int,
    val following: Int,
)
