package team.retum.usecase.usecase.banner

import team.retum.data.repository.banner.BannerRepository
import team.retum.usecase.entity.banner.toEntity
import javax.inject.Inject

class FetchBannersUseCase @Inject constructor(
    private val bannerRepository: BannerRepository,
) {
    suspend operator fun invoke() = runCatching {
        bannerRepository.fetchBanners().toEntity()
    }
}
