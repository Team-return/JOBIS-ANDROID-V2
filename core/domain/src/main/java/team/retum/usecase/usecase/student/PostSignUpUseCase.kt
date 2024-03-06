package team.retum.usecase.usecase.student

import team.retum.common.enums.Gender
import team.retum.common.enums.PlatformType
import team.retum.data.repository.student.StudentRepository
import team.retum.network.model.request.student.PostSignUpRequest
import javax.inject.Inject

class PostSignUpUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        grade: String,
        name: String,
        gender: Gender,
        classRoom: Long,
        number: Long,
        profileImageUrl: String,
    ) = runCatching {
        studentRepository.postSignUp(
            postSignUpRequest = PostSignUpRequest(
                email = email,
                password = password,
                grade = grade,
                name = name,
                gender = gender,
                classRoom = classRoom,
                number = number,
                profileImageUrl = profileImageUrl,
                platformType = PlatformType.ANDROID,
            ),
        )
    }
}
