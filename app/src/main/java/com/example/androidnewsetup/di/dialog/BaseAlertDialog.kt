package com.example.androidnewsetup.di.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import com.example.androidnewsetup.R
import com.example.androidnewsetup.databinding.DialogBaseAlertBinding

class BaseAlertDialog(context: Context) : Dialog(
    context, R.style.Dialog
) {
    private var binding: DialogBaseAlertBinding? = null
    private var latiner: ClickListener? = null
    fun setLabels(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes ok: Int,
        @StringRes cancel: Int
    ) {
        initView()
        setTitle(title)
        setMessage(message)
        setOkText(ok)
        setCancelText(cancel)
    }

    fun setLabels(title: String, message: String, ok: String, cancel: String) {
        initView()
        setTitle(title)
        setMessage(message)
        setOkText(ok)
        setCancelText(cancel)
    }

    private fun initView() {
        if (binding == null) binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_base_alert,
            null,
            false
        )
    }

    fun setListener(listener: ClickListener) {
        latiner = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        if (latiner != null) {
            binding?.callback = latiner
        }
        setContentView((binding ?: return).root)
    }

    private fun setTitle(title: String) {
        initView()
        binding?.title = title
    }

    override fun setTitle(@StringRes title: Int) {
        initView()
        binding?.title = context.getString(title)
    }

    private fun setMessage(title: String) {
        initView()
        binding?.message = title
    }

    private fun setMessage(@StringRes title: Int) {
        initView()
        binding?.message = context.getString(title)
    }

    private fun setOkText(okText: String) {
        initView()
        binding?.ok = okText
    }

    private fun setOkText(@StringRes okText: Int) {
        initView()
        binding?.ok = context.getString(okText)
    }

    private fun setCancelText(cancelText: String) {
        initView()
        binding?.cancel = cancelText
    }

    private fun setCancelText(@StringRes cancelText: Int) {
        initView()
        binding?.cancel = context.getString(cancelText)
    }

    interface ClickListener {
        fun onOkClick()
        fun onCancelClick()
    }
}