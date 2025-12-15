package team.retum.jobisandroidv2

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.HiltAndroidApp
import team.retum.common.exception.ConnectionTimeOutException
import team.retum.common.exception.OfflineException
import team.retum.common.exception.ReissueException
import team.retum.common.exception.UnknownException
import team.retum.signin.BuildConfig
import kotlin.system.exitProcess

private const val InternetErrorMsg = "인터넷 연결을 확인해주세요."
private const val TimeoutErrorMsg = "요청 시간이 초과되었습니다.\n다시 시도해주세요."
private const val LoginErrorMsg = "유저정보가 확인되지 않습니다.\n다시 로그인 해주세요."
private const val UnknownErrorMsg = "처리 중 문제가 발생했습니다."

@HiltAndroidApp
class JobisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler { _, e ->
                Firebase.crashlytics.recordException(e)
                // TODO : 에러 로그 메세지

                when (e) {
                    is OfflineException -> makeToast(InternetErrorMsg)
                    is ConnectionTimeOutException -> makeToast(TimeoutErrorMsg)
                    is ReissueException -> makeToast(LoginErrorMsg)
                    is UnknownException -> makeToast(UnknownErrorMsg)
                    else -> makeToast(UnknownErrorMsg)
                }

                val intent = this.packageManager.getLaunchIntentForPackage(this.packageName)
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this.startActivity(intent)
                    exitProcess(0)
                }
            }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }
}
