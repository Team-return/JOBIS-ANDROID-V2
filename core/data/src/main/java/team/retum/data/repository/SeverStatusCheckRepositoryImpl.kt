package team.retum.data.repository

import team.retum.network.datasource.RemoteSeverStatusCheckDataSource
import javax.inject.Inject

class SeverStatusCheckRepositoryImpl @Inject constructor(
    private val remoteSeverStatusCheckDataSource: RemoteSeverStatusCheckDataSource,
) : SeverStatusCheckRepository {
    override suspend fun severStatusCheck() {
        remoteSeverStatusCheckDataSource.severStatusCheck()
    }
}
