package team.retum.jobisandroidv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.github.anrwatchdog.ANRWatchDog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import team.retum.jobisandroidv2.ui.JobisApp
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.toast.JobisToast

private const val ANR_TIME = 10000

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var delay = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * ANR 발생 제한 시간(5s)을 10s로 늦추기 위해 사용
         * TODO: ANR이 발생하는 원인을 제거할 필요가 있음
         */
        ANRWatchDog(ANR_TIME).start()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        checkAppUpdate()
        setContent {
            BackHandler {
                setBackHandler()
            }

            JobisDesignSystemV2Theme {
                JobisApp()
            }
        }
    }

    private fun setBackHandler() {
        if (System.currentTimeMillis() > delay) {
            delay = System.currentTimeMillis() + 1000
            JobisToast.create(
                context = applicationContext,
                message = getString(R.string.close_app),
            ).show()
        } else {
            finish()
        }
    }

    private fun checkAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener {
            val isUpdateAvailable = it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            if (isUpdateAvailable && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.IMMEDIATE,
                    this,
                    0,
                )
            }
        }
    }
}
