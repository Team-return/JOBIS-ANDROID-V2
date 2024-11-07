package team.retum.jobisandroidv2

import android.content.Context
import team.retum.common.exception.ConnectionTimeOutException
import team.retum.common.exception.OfflineException
import team.retum.common.exception.ReissueException
import team.retum.common.exception.UnknownException
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.toast.JobisToast

private const val InternetErrorMsg = "인터넷 연결을 확인해주세요."
private const val TimeoutErrorMsg = "요청 시간이 초과되었습니다.\n다시 시도해주세요."
private const val LoginErrorMsg = "유저정보가 확인되지 않습니다.\n다시 로그인 해주세요."
private const val UnknownErrorMsg = "처리 중 문제가 발생했습니다.\n앱을 재시작 해주세요."

class JobisExceptionHandler(
    private val context: Context,
) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        when (e) {
            is OfflineException -> makeToast(InternetErrorMsg)
            is ConnectionTimeOutException -> makeToast(TimeoutErrorMsg)
            is ReissueException -> makeToast(LoginErrorMsg)
            is UnknownException -> makeToast(UnknownErrorMsg)
            else -> makeToast(UnknownErrorMsg)
        }
    }

    private fun makeToast(message: String) {
        JobisToast.create(
            context = context,
            message = message,
            drawable = JobisIcon.Error,
        )
    }
}
