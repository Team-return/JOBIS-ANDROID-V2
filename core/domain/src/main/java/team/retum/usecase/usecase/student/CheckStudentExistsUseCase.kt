package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import javax.inject.Inject

class CheckStudentExistsUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend operator fun invoke(
        gcn: String,
        name: String,
    ) = runCatching {
        studentRepository.checkStudentExists(
            gcn = gcn,
            name = name,
        )
    }
}
