package com.example.androidnewsetup.di.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.androidnewsetup.BR
import com.example.androidnewsetup.R

class BaseCustomDialog<V : ViewDataBinding>(
    context: Context,
    private val layoutId: Int,
    private val listener: Listener
) : Dialog(
    context, R.style.Dialog
) {
    private var binding: V? = null

    fun getBinding(): V? {
        init()
        return binding
    }

    private fun init() {
        if (binding == null) binding =
            DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
        binding?.setVariable(BR.callback, listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        binding?.root?.let { setContentView(it) }
    }

    interface Listener {
        fun onViewClick(view: View)
    }
}