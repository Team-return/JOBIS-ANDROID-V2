package team.retum.signup.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.utils.FileUtil
import team.retum.usecase.usecase.file.CreatePresignedUrlUseCase
import team.retum.usecase.usecase.file.UploadFileUseCase
import java.io.File
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
internal class SetProfileViewModel @Inject constructor(
    private val createPresignedUrlUseCase: CreatePresignedUrlUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
) : BaseViewModel<SetProfileState, SetProfileSideEffect>(SetProfileState.getInitialState()) {

    internal fun setUri(
        context: Context,
        uri: Uri?,
    ) {
        uri?.run {
            setState {
                state.value.copy(
                    uri = uri,
                    image = FileUtil.toFile(
                        context = context,
                        uri = uri,
                    ),
                    buttonEnabled = true,
                )
            }
        }
    }

    internal fun onNextClick() {
        val profileImageUrl = state.value.profileImageUrl
        postSideEffect(SetProfileSideEffect.MoveToNext(profileImageUrl = profileImageUrl))
    }

    internal fun onImgChangeClick() {
        viewModelScope.launch(Dispatchers.IO) {
            createPresignedUrlUseCase(files = listOf(state.value.image?.name ?: "")).onSuccess {
                val response = it.urls.first()
                uploadFile(
                    presignedUrl = response.preSignedUrl,
                    fileUrl = response.filePath,
                )
            }
        }
    }

    private suspend fun uploadFile(
        presignedUrl: String,
        fileUrl: String,
    ) {
        uploadFileUseCase(
            presignedUrl = presignedUrl,
            file = state.value.image!!,
        ).onSuccess {
            val encodedImageUrl = URLEncoder.encode(fileUrl, "UTF8")
            setState { state.value.copy(profileImageUrl = encodedImageUrl) }
        }
    }
}

@Immutable
internal data class SetProfileState(
    val image: File?,
    val imageUrl: String,
    val uri: Uri?,
    val profileImageUrl: String?,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = SetProfileState(
            image = null,
            imageUrl = "",
            uri = null,
            profileImageUrl = null,
            buttonEnabled = false,
        )
    }
}

internal sealed interface SetProfileSideEffect {
    data class MoveToNext(val profileImageUrl: String?) : SetProfileSideEffect
}
