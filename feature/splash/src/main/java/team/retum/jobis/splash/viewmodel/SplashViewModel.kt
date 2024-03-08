package team.retum.jobis.splash.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.usecase.auth.ReissueTokenUseCase
import team.retum.usecase.usecase.user.GetAccessTokenUseCase
import team.retum.usecase.usecase.user.GetRefreshExpiresAtUseCase
import team.retum.usecase.usecase.user.GetRefreshTokenUseCase
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getRefreshExpiresAtUseCase: GetRefreshExpiresAtUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val reissueTokenUseCase: ReissueTokenUseCase,
) : BaseViewModel<SplashState, SplashSideEffect>(SplashState.getInitialState()) {
    init {
        getAccessToken()
    }

    private fun getAccessToken() {
        viewModelScope.launch(Dispatchers.IO) {
            getAccessTokenUseCase().onSuccess {
                checkRefreshTokenExpired()
            }.onFailure {
                when (it) {
                    is NullPointerException -> {
                        postSideEffect(SplashSideEffect.MoveToSignIn)
                    }
                }
            }
        }
    }

    private fun checkRefreshTokenExpired() {
        getRefreshExpiresAtUseCase().onSuccess { refreshExpiresAt ->
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(refreshExpiresAt))) {
                postSideEffect(SplashSideEffect.MoveToSignIn)
            } else {
                reissueToken()
            }
        }.onFailure {
            when (it) {
                is NullPointerException -> {
                    postSideEffect(SplashSideEffect.MoveToSignIn)
                }
            }
        }
    }

    private fun reissueToken() {
        getRefreshToken()
        viewModelScope.launch(Dispatchers.IO) {
            reissueTokenUseCase(refreshToken = state.value.refreshToken!!).onSuccess {
                postSideEffect(SplashSideEffect.MoveToMain)
            }
        }
    }

    private fun getRefreshToken() {
        getRefreshTokenUseCase().onSuccess {
            setState { state.value.copy(refreshToken = it) }
        }.onFailure {
            when (it) {
                is NullPointerException -> {
                    postSideEffect(SplashSideEffect.MoveToSignIn)
                }
            }
        }
    }
}

internal data class SplashState(
    val accessToken: String?,
    val refreshToken: String?,
) {
    companion object {
        fun getInitialState() = SplashState(
            accessToken = null,
            refreshToken = null,
        )
    }
}

internal sealed interface SplashSideEffect {
    data object MoveToSignIn : SplashSideEffect
    data object MoveToMain : SplashSideEffect
}
