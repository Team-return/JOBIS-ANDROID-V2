package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import team.retum.network.model.request.student.EditProfileImageRequest
import javax.inject.Inject

class EditProfileImageUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke(profileImageUrl: String) = runCatching {
        studentRepository.editProfileImage(
            editProfileImageRequest = EditProfileImageRequest(profileImageUrl),
        )
    }
}
