package team.retum.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val STATE_DELAY = 3000L

abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _sideEffect: MutableSharedFlow<E> = MutableSharedFlow()
    val sideEffect = _sideEffect.asSharedFlow()

    protected fun setState(newState: () -> S) {
        _state.value = newState()
    }

    protected fun postSideEffect(sideEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            this@BaseViewModel._sideEffect.emit(sideEffect)
        }
    }

    protected suspend fun freezeState(
        before: S,
        after: S,
    ) {
        setState { before }
        delay(STATE_DELAY)
        setState { after }
    }
}
