package team.retum.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val MAX_TIMER_SECONDS = 300

class TimerUtil {

    private val _remainTime: MutableStateFlow<String> = MutableStateFlow("5:00")
    val remainTime = _remainTime.asStateFlow()

    private lateinit var timer: Job

    init {
        setTimer()
    }

    fun startTimer() {
        timer.start()
    }

    fun stopTimer() {
        timer.cancel()
    }

    fun setTimer() {
        timer = CoroutineScope(Dispatchers.IO).launch {
            repeat(MAX_TIMER_SECONDS) {
                val remainTime = MAX_TIMER_SECONDS - it - 1
                val minute = remainTime / 60
                val second = remainTime - minute * 60
                _remainTime.emit("0$minute:${if (second < 10) 0 else ""}$second")
                delay(1000L)
            }
        }
    }
}
