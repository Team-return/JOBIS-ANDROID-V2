package team.retum.usecase.usecase.user

import team.retum.jobis.local.datasource.user.LocalUserDataSource
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) {
    operator fun invoke() = runCatching {
        localUserDataSource.getAccessToken()
    }
}
