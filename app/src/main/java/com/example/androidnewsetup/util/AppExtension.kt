package com.example.androidnewsetup.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

//Image view
@SuppressLint("CheckResult")
@BindingAdapter("imageUrl", "placeholder", "viewWidth", "viewHeight", requireAll = false)
fun ImageView.loadImage(
    url: String,
    placeholder: Drawable?,
    view_width: Int?,
    view_height: Int?
) {
    val options = RequestOptions()
    if (view_width != null && view_height != null) options.override(view_width, view_height)
    if (placeholder != null) options.placeholder(placeholder)
    Glide.with(this.context).load(url).apply(options).into(this)
}

@BindingAdapter(value = ["simpleResource"])
fun setStepGroupIcon(imageView: ImageView?, simpleResource: Int) {
    if (simpleResource != -1) {
        imageView?.setImageResource(simpleResource)
    }
}

private fun getPathFromUri(uriString: String, context: Context): String {
    return UriUtils.getPathFromUri(context, Uri.parse(uriString)).toString()
}


//Log for activity
fun Activity.loggerD(msg: String) {
    Log.d(this.localClassName, msg)
}

fun Activity.loggerE(msg: String) {
    Log.e(this.localClassName, msg)
}

fun Activity.loggerI(msg: String) {
    Log.i(this.localClassName, msg)
}

fun Activity.loggerV(msg: String) {
    Log.v(this.localClassName, msg)
}

fun Activity.loggerW(msg: String) {
    Log.w(this.localClassName, msg)
}

//Log for fragment
fun Fragment.loggerD(msg: String) {
    Log.d(this.javaClass.simpleName, msg)
}

fun Fragment.loggerE(msg: String) {
    Log.e(this.javaClass.simpleName, msg)
}

fun Fragment.loggerI(msg: String) {
    Log.i(this.javaClass.simpleName, msg)
}

fun Fragment.loggerV(msg: String) {
    Log.v(this.javaClass.simpleName, msg)
}

fun Fragment.loggerW(msg: String) {
    Log.w(this.javaClass.simpleName, msg)
}