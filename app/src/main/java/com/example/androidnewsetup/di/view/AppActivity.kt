package com.example.androidnewsetup.di.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidnewsetup.R
import com.example.androidnewsetup.BuildConfig
import com.example.androidnewsetup.util.UserManager
import com.example.androidnewsetup.util.loader.SpinnerLoader
import com.example.androidnewsetup.util.message.MessageUtils
import org.aviran.cookiebar2.CookieBar

abstract class AppActivity : AppCompatActivity() {
    private var progressDialog: SpinnerLoader? = null

    var appDataManager: UserManager? = null

    //Custom message
    fun msgNormal(msg: String) {
        MessageUtils.normal(baseContext, msg)
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
            progressDialog = SpinnerLoader(this)
        }
        progressDialog?.show()
    }

    protected fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    //Start and finish activity
    protected open fun startNewActivity(intent: Intent, finishExisting: Boolean) {
        startNewActivity(intent, finishExisting, true)
    }

    fun startNewActivity(
        intent: Intent,
        finishExisting: Boolean,
        animate: Boolean
    ) {
        startActivity(intent)
        if (finishExisting) finish()
        if (animate) animateActivity()

    }

    protected open fun startNewActivity(intent: Intent) {
        startNewActivity(intent, finishExisting = false, animate = true)
    }

    open fun animateActivity() {
        if (BuildConfig.EnableAnim) overridePendingTransition(
            R.anim.activity_in,
            R.anim.activity_out
        )
    }

    protected open fun finish(animate: Boolean) {
        finish()
        if (BuildConfig.EnableAnim && animate) overridePendingTransition(
            R.anim.activity_back_in,
            R.anim.activity_back_out
        )
    }

    //Back press
    protected open fun onBackPressed(animate: Boolean) {
        super.onBackPressed()
        if (BuildConfig.EnableAnim && animate) overridePendingTransition(
            R.anim.activity_back_in,
            R.anim.activity_back_out
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    //Set Cookie
    private fun showCookie(msg: String, backgroundColor: Int) {
        CookieBar.build(this)
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

}