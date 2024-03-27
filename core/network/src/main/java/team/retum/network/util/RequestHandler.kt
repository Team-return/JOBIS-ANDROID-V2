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
