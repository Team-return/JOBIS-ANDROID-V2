package team.retum.jobisandroidv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.github.anrwatchdog.ANRWatchDog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.retum.device.DeviceTokenManager
import team.retum.jobis.R
import team.retum.jobisandroidv2.ui.JobisApp
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import javax.inject.Inject

private const val ANR_TIME = 10000

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var delay = 0L

    @Inject
    lateinit var deviceTokenManager: DeviceTokenManager

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode != RESULT_OK) {

        }
    }

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

        lifecycleScope.launch {
            deviceTokenManager.fetchDeviceToken()
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

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() != UpdateAvailability.UPDATE_AVAILABLE) {
                return@addOnSuccessListener
            }

            val updateOptions = AppUpdateOptions
                .newBuilder(AppUpdateType.IMMEDIATE)
                .setAllowAssetPackDeletion(true)
                .build()

            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateLauncher,
                updateOptions,
            )
        }
    }
}
