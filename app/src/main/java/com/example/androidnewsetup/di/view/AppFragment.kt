package com.example.androidnewsetup.di.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.UserManager
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidnewsetup.BuildConfig
import com.example.androidnewsetup.R
import com.example.androidnewsetup.util.loader.SpinnerLoader
import com.example.androidnewsetup.util.message.MessageUtils
import org.aviran.cookiebar2.CookieBar

abstract class AppFragment : Fragment() {

    private var progressDialog: SpinnerLoader? = null
    lateinit var userManager: UserManager

    //Custom message
    fun msgNormal(msg: String) {
        MessageUtils.normal(requireContext(), msg)
    }

    fun msgSuccess(msg: String) {
        showCookie(msg, R.color.successColor)
    }

    fun msgInfo(msg: String) {
        showCookie(msg, R.color.infoColor)
    }

    fun msgWarning(msg: String) {
        showCookie(msg, R.color.warningColor)
    }

    fun msgError(msg: String) {
        showCookie(msg, R.color.errorColor)
    }

    //Progress dialog
    protected fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = SpinnerLoader(requireActivity())
        }
        progressDialog?.show()
    }

    protected fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    protected open fun startNewActivity(intent: Intent, finishExisting: Boolean) {
        try {
            startActivity(intent)
            if (finishExisting) requireActivity().finish()
            if (BuildConfig.EnableAnim) requireActivity().overridePendingTransition(
                R.anim.activity_in,
                R.anim.activity_out
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Start and finish activity
    fun startNewActivity(
        intent: Intent,
        finishExisting: Boolean,
        animate: Boolean
    ) {
        try {
            startActivity(intent)
            if (finishExisting) requireActivity().finish()
            if (BuildConfig.EnableAnim && animate) animateActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun startNewActivity(intent: Intent) {
        startNewActivity(intent, finishExisting = false, animate = true)
    }

    open fun animateActivity() {
        if (BuildConfig.EnableAnim) requireActivity().overridePendingTransition(
            R.anim.activity_in,
            R.anim.activity_out
        )
    }

    //Set Cookie
    private fun showCookie(msg: String, backgroundColor: Int) {
        CookieBar.build(requireActivity())
            .setCustomView(R.layout.view_custom_toast)
            .setCustomViewInitializer {
                val tvMessage: TextView = it.findViewById(R.id.tv_message)
                tvMessage.isSelected = true
            }
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(msg)
            .setBackgroundColor(backgroundColor)
            .setDuration(3000)
            .show()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }
}