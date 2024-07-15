package team.retum.jobisdesignsystemv2.popup

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.DrawableRes
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.foundation.JobisIcon

object JobisPopup {
    /**
     * JOBIS에서 사용하는 커스텀 팝업
     *
     * 아이콘, 메시지와 버튼을 포함하고 있는 xml 뷰
     *
     * @param context [JobisPopup]에서 사용될 [Context]
     * @param message [JobisPopup]에서 보여질 메시지
     * @param onClick [JobisPopup]의 버튼이 클릭될 때 동작하는 함수
     * @param buttonText 버튼에 표시될 텍스트
     * @param gravity [JobisPopup]이 표시될 기준
     * - default value : [Gravity.TOP]
     * @param drawable [JobisPopup]에서 사용될 drawable resource의 id
     */
    fun showPopup(
        context: Context,
        message: String,
        onClick: () -> Unit,
        buttonText: String,
        gravity: Int = Gravity.TOP,
        iconColor: Int = R.color.primary30,
        @DrawableRes drawable: Int = JobisIcon.Download,
    ) {
        val popupView: View = LayoutInflater.from(context).inflate(R.layout.jobis_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )
        val slideInAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_slide_in)
        popupView.startAnimation(slideInAnimation)

        val slideOutAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_slide_out)

        popupView.apply {
            findViewById<ImageView>(R.id.img_popup_icon).setColorFilter(iconColor)
            findViewById<ImageView>(R.id.img_popup_icon).setImageResource(drawable)
            findViewById<TextView>(R.id.tv_popup_message).text = message
            findViewById<Button>(R.id.btn_popup).setOnClickListener { onClick() }
            findViewById<Button>(R.id.btn_popup).text = buttonText
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                popupView.startAnimation(slideOutAnimation)
            },
            3500,
        )

        Handler(Looper.getMainLooper()).postDelayed(
            {
                popupWindow.dismiss()
            },
            4000,
        )

        popupWindow.showAtLocation(popupView, gravity, 0, 180)
    }
}
