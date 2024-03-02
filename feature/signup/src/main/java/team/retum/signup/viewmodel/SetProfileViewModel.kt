package team.retum.signup.viewmodel

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class SetProfileViewModel @Inject constructor(

) : BaseViewModel<SetProfileState, SetProfileSideEffect>(SetProfileState.getInitialState()) {

    internal fun setUri(uri: Uri?) = setState {
        state.value.copy(
            uri = uri,
            buttonEnabled = uri != null,
        )
    }

    internal fun onNextClick() {

    }
}

internal data class SetProfileState(
    val image: File?,
    val imageUrl: String,
    val uri: Uri?,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = SetProfileState(
            image = null,
            imageUrl = "",
            uri = null,
            buttonEnabled = false,
        )
    }
}

internal sealed interface SetProfileSideEffect {

}
