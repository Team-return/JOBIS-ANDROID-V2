package team.retum.network.util

import retrofit2.HttpException
import team.retum.common.exception.BadRequestException
import team.retum.common.exception.CheckServerException
import team.retum.common.exception.ConflictException
import team.retum.common.exception.ConnectionTimeOutException
import team.retum.common.exception.ForbiddenException
import team.retum.common.exception.MethodNotAllowedException
import team.retum.common.exception.NotFoundException
import team.retum.common.exception.OfflineException
import team.retum.common.exception.ServerException
import team.retum.common.exception.TooManyRequestException
import team.retum.common.exception.UnAuthorizedException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * JOBIS에서 네트워크 요청의 예외 처리를 위해 사용되는 Handler.
 *
 * @param T 사용할 요청에서 반환하는 반환 타입
 *
 * 별도의 response class가 존재하는 경우 다음과 같이 작성한다.
 * ```
 * override suspend fun signIn(signInRequest: SignInRequest): TokenResponse {
 *     return RequestHandler<TokenResponse>().request {
 *         userApi.signIn(signInRequest = signInRequest)
 *     }
 * }
 * ```
 * response가 존재하지 않는 경우 다음과 같이 작성한다.
 * ```
 * override suspend fun applyCompany(
 *     recruitmentId: Long,
 *     applyCompanyRequest: ApplyCompanyRequest,
 * ) {
 *     RequestHandler<Unit>().request {
 *         applicationApi.applyCompany(
 *             recruitmentId = recruitmentId,
 *             applyCompanyRequest = applyCompanyRequest,
 *         )
 *    }
 * }
 * ```
 */
class RequestHandler<T> {
    suspend fun request(block: suspend () -> T): T =
        try {
            block()
        } catch (e: HttpException) {
            throw when (e.code()) {
                400 -> BadRequestException
                401 -> UnAuthorizedException
                403 -> ForbiddenException
                404 -> NotFoundException
                405 -> MethodNotAllowedException
                409 -> ConflictException
                429 -> TooManyRequestException
                502 -> CheckServerException
                in 500..599 -> ServerException
                else -> e
            }
        } catch (e: SocketTimeoutException) {
            throw ConnectionTimeOutException
        } catch (e: UnknownHostException) {
            throw OfflineException
        } catch (e: Throwable) {
            throw e
        }
}
