package team.retum.jobis.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.Department
import team.retum.common.exception.BadRequestException
import team.retum.common.utils.FileUtil
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.company.ReviewableCompaniesEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import team.retum.usecase.usecase.company.FetchReviewableCompaniesUseCase
import team.retum.usecase.usecase.file.CreatePresignedUrlUseCase
import team.retum.usecase.usecase.file.UploadFileUseCase
import team.retum.usecase.usecase.student.EditProfileImageUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import team.retum.usecase.usecase.user.SignOutUseCase
import javax.inject.Inject

@HiltViewModel
internal class MyPageViewModel @Inject constructor(
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val fetchReviewableCompaniesUseCase: FetchReviewableCompaniesUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val createPresignedUrlUseCase: CreatePresignedUrlUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val editProfileImageUseCase: EditProfileImageUseCase,
) : BaseViewModel<MyPageState, MyPageSideEffect>(MyPageState.getInitialState()) {

    init {
        fetchStudentInformation()
        fetchReviewableCompanies()
    }

    internal fun setShowSignOutModal(showSignOutModal: Boolean) = setState {
        state.value.copy(showSignOutModal = showSignOutModal)
    }

    internal fun setShowWithdrawalModal(showWithdrawalModal: Boolean) = setState {
        state.value.copy(showWithdrawalModal = showWithdrawalModal)
    }

    internal fun setUri(
        uri: Uri?,
        context: Context,
    ) {
        uri?.run {
            val file = FileUtil.toFile(
                context = context,
                uri = uri,
            )
            viewModelScope.launch(Dispatchers.IO) {
                createPresignedUrlUseCase(files = listOf(file.name)).onSuccess {
                    uploadFileUseCase(
                        presignedUrl = it.urls.first().preSignedUrl,
                        file = file,
                    )
                    val profileImageUrl = it.urls.first().filePath
                    editProfileImage(profileImageUrl = profileImageUrl)
                }
            }
        }
    }

    private suspend fun editProfileImage(profileImageUrl: String) {
        editProfileImageUseCase(profileImageUrl = profileImageUrl).onSuccess {
            fetchStudentInformation()
            postSideEffect(MyPageSideEffect.SuccessEditProfileImage)
        }.onFailure {
            when (it) {
                is BadRequestException -> {
                    postSideEffect(MyPageSideEffect.BadEditProfileImage)
                }

                is KotlinNullPointerException -> {
                    fetchStudentInformation()
                    postSideEffect(MyPageSideEffect.SuccessEditProfileImage)
                }
            }
        }
    }

    private fun fetchStudentInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStudentInformationUseCase().onSuccess {
                val profileImageUrl = ResourceKeys.IMAGE_URL + it.profileImageUrl
                setState { state.value.copy(studentInformation = it.copy(profileImageUrl = profileImageUrl)) }
            }
        }
    }

    private fun fetchReviewableCompanies() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewableCompaniesUseCase().onSuccess {
                setState { state.value.copy(reviewableCompany = it.companies.firstOrNull()) }
            }
        }
    }

    internal fun onSignOutClick() {
        signOutUseCase().onSuccess {
            postSideEffect(MyPageSideEffect.SuccessSignOut)
        }
    }

    internal fun onWithdrawalClick() {
        // TODO 회원탈퇴 로직 구현
    }
}

internal data class MyPageState(
    val studentInformation: StudentInformationEntity,
    val showSignOutModal: Boolean,
    val showWithdrawalModal: Boolean,
    val reviewableCompany: ReviewableCompaniesEntity.ReviewableCompanyEntity?,
) {
    companion object {
        fun getInitialState() = MyPageState(
            studentInformation = StudentInformationEntity(
                studentName = "",
                studentGcn = "",
                department = Department.SOFTWARE_DEVELOP,
                profileImageUrl = "",
            ),
            showSignOutModal = false,
            showWithdrawalModal = false,
            reviewableCompany = null,
        )
    }
}

internal sealed interface MyPageSideEffect {
    data object SuccessSignOut : MyPageSideEffect
    data object SuccessEditProfileImage : MyPageSideEffect
    data object BadEditProfileImage : MyPageSideEffect
}
