package team.retum.usecase.usecase.student

import team.retum.data.repository.student.StudentRepository
import team.retum.network.model.response.RecentResponse
import javax.inject.Inject

class FetchRecentCompaniesUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
) {
    suspend fun invoke(): RecentResponse {
        return studentRepository.fetchRecentCompanies()
    }
}
