package com.tymeX.github.ui.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tymeX.github.mapper.toUiState
import com.tymex.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val usersPaging: StateFlow<PagingData<UserUiState>> =
        userRepository.getUsers()
            .map { pagingData -> pagingData.map { it.toUiState() } }
            .cachedIn(viewModelScope)
            .stateIn(
                viewModelScope, started = SharingStarted.WhileSubscribed(5000),
                initialValue = PagingData.empty()
            )
}

data class UserUiState(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
)

