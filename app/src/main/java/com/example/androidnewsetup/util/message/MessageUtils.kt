package com.example.androidnewsetup.util.message

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.example.androidnewsetup.R
import com.google.android.material.snackbar.Snackbar

object MessageUtils {
    @ColorInt
    private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    @ColorInt
    private val ERROR_COLOR = Color.parseColor("#D50000")

    @ColorInt
    private val INFO_COLOR = Color.parseColor("#08aad2")

    @ColorInt
    private val SUCCESS_COLOR = Color.parseColor("#388E3C")

    @ColorInt
    private val WARNING_COLOR = Color.parseColor("#FFA900")

    @ColorInt
    private val NORMAL_COLOR = Color.parseColor("#353A3E")

    private const val tintIcon = false


    fun showSnackBar(view: View?, message: String?) {
        if (view == null || message == null) {
            return
        }
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun normal(context: Context, message: String) {
        val t = custom(context, message, null, Toast.LENGTH_SHORT, false)
        t.show()
    }

    fun success(context: Context, message: String) {
        val t = custom(
            context, message, getDrawable(context, R.drawable.ic_toast_checked),
            SUCCESS_COLOR, Toast.LENGTH_SHORT, withIcon = true, shouldTint = true
        )
        t.show()
    }

    fun warning(context: Context, message: String) {
        val t = custom(
            context, message, getDrawable(context, R.drawable.ic_toast_warn),
            WARNING_COLOR, Toast.LENGTH_SHORT, withIcon = true, shouldTint = true
        )
        t.show()
    }

    fun info(context: Context, message: String) {
        val t = custom(
            context, message, getDrawable(context, R.drawable.ic_toast_info),
            INFO_COLOR, Toast.LENGTH_SHORT, withIcon = true, shouldTint = true
        )
        t.show()
    }

    fun error(context: Context, message: String) {
        val t = custom(
            context, message, getDrawable(context, R.drawable.ic_toast_block),
            ERROR_COLOR, Toast.LENGTH_SHORT, withIcon = true, shouldTint = true
        )
        t.show()
    }

    @CheckResult
    fun custom(
        context: Context, message: String, icon: Drawable?,
        duration: Int, withIcon: Boolean
    ): Toast {
        return custom(context, message, icon, -1, duration, withIcon, false)
    }

    @CheckResult
    fun custom(
        context: Context, message: String,
        @DrawableRes iconRes: Int, @ColorInt tintColor: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        return custom(
            context,
            message,
            getDrawable(context, iconRes),
            tintColor,
            duration,
            withIcon,
            shouldTint
        )
    }

    @CheckResult
    fun custom(
        context: Context, message: String, icon: Drawable?,
        @ColorInt tintColor: Int, duration: Int, withIcon: Boolean,
        shouldTint: Boolean
    ): Toast {
        val currentToast = Toast.makeText(context, "", duration)
        @SuppressLint("InflateParams") val toastLayout = (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.custom_toast, null)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)
        val toastTextView = toastLayout.findViewById<TextView>(R.id.toast_text)
        val drawableFrame: Drawable? = if (shouldTint) tint9PatchDrawableFrame(
            context,
            tintColor
        ) else getDrawable(context, R.drawable.toast_frame)
        setBackground(toastLayout, drawableFrame)
        if (withIcon) {
            requireNotNull(icon) { "Avoid passing 'icon' as null if 'withIcon' is set to true" }
            if (tintIcon) {
                tintIcon(icon, DEFAULT_TEXT_COLOR)
            }
            setBackground(toastIcon, icon)
        } else {
            toastIcon.visibility = View.GONE
        }
        toastTextView.text = message
        toastTextView.setTextColor(DEFAULT_TEXT_COLOR)
        currentToast.view = toastLayout
        return currentToast
    }

    private fun setBackground(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    private fun tintIcon(drawable: Drawable, @ColorInt tintColor: Int): Drawable {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    private fun tint9PatchDrawableFrame(
        context: Context,
        @ColorInt tintColor: Int
    ): Drawable {
        val toastDrawable = getDrawable(context, R.drawable.toast_frame) as NinePatchDrawable?
        return tintIcon(toastDrawable!!, tintColor)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return context.getDrawable(id)
    }
}