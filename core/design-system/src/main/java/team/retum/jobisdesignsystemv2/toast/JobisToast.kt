package team.retum.jobisdesignsystemv2.toast

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.foundation.JobisIcon

object JobisToast {
    /**
     * JOBIS에서 사용하는 토스트이다.
     *
     * @param context 토스트 표시에 사용될 [Context]
     * @param message 토스트에 표시될 메시지
     * @param duration 토스트 표시 시간
     * @param drawable 토스트에 텍스트와 함께 표시될 drawable의 resource id
     * @return 위 파라미터들로 만들어진 [Toast] 객체
     *
     * 다음과 같이 사용한다.
     * ```
     * JobisToast.create(
     *     context = context,
     *     message = context.getString(R.string.toast_unexpected_grade),
     *     drawable = JobisIcon.Error,
     * ).show()
     * ```
     */
    fun create(
        context: Context,
        message: String,
        duration: Int = Toast.LENGTH_SHORT,
        @DrawableRes drawable: Int = JobisIcon.CheckCircle,
    ): Toast {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(
            R.layout.jobis_toast,
            LinearLayout(context).findViewById(R.id.ll_jobis_toast),
        )

        view.findViewById<TextView>(R.id.tv_toast_message).text = message
        view.findViewById<ImageView>(R.id.img_toast_icon).setImageResource(drawable)

        return Toast(context).apply {
            this.duration = duration
            this.view = view
        }
    }
}
