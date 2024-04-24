package team.retum.jobisdesignsystemv2.popup

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.DrawableRes
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.foundation.JobisIcon

object JobisPopup {

    fun showPopup(
        context: Context,
        message: String,
        onClick: () -> Unit,
        buttonText: String,
        iconColor: Int = R.color.primary30,
        @DrawableRes drawable: Int = JobisIcon.AppLogo,
    ) {
        val popupView: View = LayoutInflater.from(context).inflate(R.layout.jobis_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

        popupView.findViewById<ImageView>(R.id.img_popup_icon).setColorFilter(iconColor)
        popupView.findViewById<ImageView>(R.id.img_popup_icon).setImageResource(drawable)
        popupView.findViewById<TextView>(R.id.tv_popup_message).text = message
        popupView.findViewById<Button>(R.id.btn_popup).setOnClickListener { onClick() }
        popupView.findViewById<Button>(R.id.btn_popup).text = buttonText

        Handler(Looper.getMainLooper()).postDelayed(
            {
                popupWindow.dismiss()
            },
            4000,
        )

        popupWindow.showAtLocation(popupView, 0, 0, 180)
    }
}
