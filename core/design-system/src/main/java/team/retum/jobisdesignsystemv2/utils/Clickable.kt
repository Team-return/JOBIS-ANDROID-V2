package team.retum.jobisdesignsystemv2.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter

internal const val DURATION_MILLIS = 200
internal const val DEFAULT_PRESS_DEPTH = 0.98f
internal const val MIN_PRESS_DEPTH = 1f
internal const val DEFAULT_DISABLED_MILLIS = 300L

/**
 * JOBIS에서 클릭 이벤트 및 효과를 표현할 때 사용하는 [Modifier] 확장 함수
 *
 * @param enabled 클릭 활성화 여부
 * @param pressDepth 클릭 애니메이션의 깊이 정도
 * @param onPressed 요소가 눌러졌을 때 동작하는 함수
 * @param onClick 요소를 클릭했을 때 동작하는 함수
 * @param disabledMillis 요소 클릭 후 잠시 비활성화 될 시간
 * @return 클릭, 애니메이션 등이 적용된 [Modifier]
 */
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("ComposableNaming")
@Composable
fun Modifier.clickable(
    enabled: Boolean = true,
    pressDepth: Float = DEFAULT_PRESS_DEPTH,
    onPressed: ((pressed: Boolean) -> Unit)? = null,
    onClick: () -> Unit,
    disabledMillis: Long = DEFAULT_DISABLED_MILLIS,
): Modifier {
    var pressed by remember { mutableStateOf(false) }
    var lastClick by remember { mutableLongStateOf(0L) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) {
            pressDepth
        } else {
            1f
        },
        animationSpec = tween(durationMillis = DURATION_MILLIS),
        label = "",
    )

    return this then Modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInteropFilter { event ->
            if (enabled) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressed = true
                        onPressed?.invoke(true)
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        pressed = false
                        onPressed?.invoke(false)
                        if (event.action == MotionEvent.ACTION_UP && System.currentTimeMillis() - lastClick >= disabledMillis) {
                            lastClick = System.currentTimeMillis()
                            onClick()
                        }
                    }
                }
            }
            true
        }
}
