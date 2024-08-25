package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import javax.inject.Inject

class ComparePasswordUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke(password: String) = runCatching {
        studentRepository.comparePassword(password = password)
    }
}
