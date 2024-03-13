package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import team.retum.network.model.request.student.ForgottenPasswordRequest
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) = runCatching {
        studentRepository.resetPassword(
            forgottenPasswordRequest = ForgottenPasswordRequest(
                email = email,
                password = password,
            ),
        )
    }
}
