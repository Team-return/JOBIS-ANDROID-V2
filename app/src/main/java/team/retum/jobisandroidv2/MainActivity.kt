package team.retum.jobisandroidv2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.retum.device.DeviceTokenManager
import team.retum.jobis.R
import team.retum.jobisandroidv2.notification.PushNotificationData
import team.retum.jobisandroidv2.ui.JobisApp
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var delay = 0L
    private var pushNotificationData by mutableStateOf<PushNotificationData?>(null)
    private var isPushNotificationHandled = false

    @Inject
    lateinit var deviceTokenManager: DeviceTokenManager

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode != RESULT_OK) {
//      TODO :: 업데이트 실패 시 모달 호출
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        checkAppUpdate()
        if (savedInstanceState == null) {
            handlePushNotification(intent)
        } else {
            restorePushNotification(savedInstanceState)
        }
        setContent {
            BackHandler {
                setBackHandler()
            }

            JobisDesignSystemV2Theme {
                JobisApp(
                    pushNotificationData = pushNotificationData,
                    onPushNotificationHandled = {
                        pushNotificationData = null
                        isPushNotificationHandled = true
                    },
                )
            }
        }

        lifecycleScope.launch {
            deviceTokenManager.fetchDeviceToken()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handlePushNotification(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(PUSH_NOTIFICATION_HANDLED, isPushNotificationHandled)
        pushNotificationData?.let {
            outState.putBoolean(HAS_PENDING_PUSH_NOTIFICATION, true)
            it.saveState(outState)
        }
    }

    private fun handlePushNotification(intent: Intent?) {
        PushNotificationData.fromIntent(intent)?.let {
            pushNotificationData = it
            isPushNotificationHandled = false
        }
    }

    private fun restorePushNotification(savedInstanceState: Bundle) {
        isPushNotificationHandled = savedInstanceState.getBoolean(PUSH_NOTIFICATION_HANDLED)
        if (!isPushNotificationHandled && savedInstanceState.getBoolean(HAS_PENDING_PUSH_NOTIFICATION)) {
            pushNotificationData = PushNotificationData.fromSavedState(savedInstanceState)
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

    private companion object {
        const val HAS_PENDING_PUSH_NOTIFICATION = "has_pending_push_notification"
        const val PUSH_NOTIFICATION_HANDLED = "push_notification_handled"
    }
}
