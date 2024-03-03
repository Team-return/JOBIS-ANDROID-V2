package team.returm.mypage.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.Department
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.company.ReviewableCompaniesEntity
import team.retum.usecase.entity.student.StudentInformationEntity
import team.retum.usecase.usecase.company.FetchReviewableCompaniesUseCase
import team.retum.usecase.usecase.student.FetchStudentInformationUseCase
import team.retum.usecase.usecase.user.SignOutUseCase
import javax.inject.Inject

@HiltViewModel
internal class MyPageViewModel @Inject constructor(
    private val fetchStudentInformationUseCase: FetchStudentInformationUseCase,
    private val fetchReviewableCompaniesUseCase: FetchReviewableCompaniesUseCase,
    private val signOutUseCase: SignOutUseCase,
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
                setState { state.value.copy(reviewableCompany = it.companies.first()) }
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
    data object SuccessSignOut: MyPageSideEffect
}
