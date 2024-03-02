package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import team.retum.usecase.entity.student.toEntity
import javax.inject.Inject

class FetchStudentInformationUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke() = runCatching {
        studentRepository.fetchStudentInformation().toEntity()
    }
}
