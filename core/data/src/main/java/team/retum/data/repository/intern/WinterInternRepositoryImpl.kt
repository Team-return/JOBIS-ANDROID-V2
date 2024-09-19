package team.retum.data.repository.intern

import team.retum.network.datasource.winter.WinterInternDataSource
import javax.inject.Inject

class WinterInternRepositoryImpl @Inject constructor(
    private val winterInternDataSource: WinterInternDataSource,
) : WinterInterRepository {
    override suspend fun fetchWinterIntern(): Boolean =
        winterInternDataSource.fetchWinterIntern()
}
