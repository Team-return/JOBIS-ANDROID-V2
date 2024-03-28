package team.retum.jobis.splash.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.CheckServerException
import team.retum.common.exception.ConnectionTimeOutException
import team.retum.common.exception.NotFoundException
import team.retum.common.exception.OfflineException
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

    internal fun getAccessToken() {
        viewModelScope.launch(Dispatchers.IO) {
            getAccessTokenUseCase().onSuccess {
                if (it.isBlank()) {
                    postSideEffect(SplashSideEffect.MoveToLanding)
                } else {
                    checkRefreshTokenExpired()
                }
            }.onFailure {
                when (it) {
                    is NullPointerException -> {
                        postSideEffect(SplashSideEffect.MoveToLanding)
                    }

                    is CheckServerException -> {
                        postSideEffect(SplashSideEffect.ShowCheckServerDialog)
                    }
                }
            }
        }
    }

    private fun checkRefreshTokenExpired() {
        getRefreshExpiresAtUseCase().onSuccess { refreshExpiresAt ->
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(refreshExpiresAt))) {
                postSideEffect(SplashSideEffect.MoveToLanding)
            } else {
                reissueToken()
            }
        }.onFailure {
            when (it) {
                is NullPointerException -> {
                    postSideEffect(SplashSideEffect.MoveToLanding)
                }
            }
        }
    }

    private fun reissueToken() {
        getRefreshToken()
        viewModelScope.launch(Dispatchers.IO) {
            reissueTokenUseCase(refreshToken = state.value.refreshToken!!).onSuccess {
                postSideEffect(SplashSideEffect.MoveToMain)
            }.onFailure {
                when (it) {
                    is NotFoundException -> {
                        postSideEffect(SplashSideEffect.MoveToLanding)
                    }

                    is OfflineException -> {
                        postSideEffect(SplashSideEffect.ShowOfflineToast)
                    }

                    is ConnectionTimeOutException -> {
                        postSideEffect(SplashSideEffect.ShowCheckServerDialog)
                    }
                }
            }
        }
    }

    private fun getRefreshToken() {
        getRefreshTokenUseCase().onSuccess {
            setState { state.value.copy(refreshToken = it) }
        }.onFailure {
            when (it) {
                is NullPointerException -> {
                    postSideEffect(SplashSideEffect.MoveToLanding)
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
    data object MoveToLanding : SplashSideEffect
    data object MoveToMain : SplashSideEffect
    data object ShowCheckServerDialog : SplashSideEffect
    data object ShowOfflineToast : SplashSideEffect
}
