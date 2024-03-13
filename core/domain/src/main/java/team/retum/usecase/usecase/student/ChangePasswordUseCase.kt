package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import team.retum.network.model.request.student.ChangePasswordRequest
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
    ) = runCatching {
        studentRepository.changePassword(
            changePasswordRequest = ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword,
            ),
        )
    }
}
