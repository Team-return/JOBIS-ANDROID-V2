package team.retum.jobisandroidv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import team.returm.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobisDesignSystemV2Theme {
                JobisApp()
            }
        }
    }
}
