package com.example.androidnewsetup.di.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidnewsetup.BR


class SimpleRecyclerViewPagingAdapter<M : Any, B : ViewDataBinding> : PagingDataAdapter<M, SimpleRecyclerViewPagingAdapter.SimpleViewHolder<B>> {

    @LayoutRes
    private val layoutResId: Int
    private val modelVariableId: Int
    private var callback: SimpleCallback<M, B>? = null

    interface SimpleCallback<M, B : ViewDataBinding> {
        fun onItemClick(v: View, m: M) {}
        fun onItemClick(v: View, m: M, pos: Int) {}
        fun onItemClick(v: View, m: M, pos: Int, holder: SimpleViewHolder<B>)
        fun onPositionClick(v: View, pos: Int) {}
        fun onViewBinding(holder: SimpleViewHolder<B>, m: M, pos: Int) {}
    }

    constructor(@LayoutRes layoutResId: Int, modelVariableId: Int, callback: SimpleCallback<M, B>, listener: DiffUtil.ItemCallback<M>) : super(listener) {
        this.layoutResId = layoutResId
        this.modelVariableId = modelVariableId
        this.callback = callback
    }

    constructor(@LayoutRes layoutResId: Int, modelVariableId: Int, listener: DiffUtil.ItemCallback<M>) : super(listener) {
        this.layoutResId = layoutResId
        this.modelVariableId = modelVariableId
    }

    fun setCallback(callback: SimpleCallback<M, B>) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<B> {
        val binding: B =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
        if (callback != null) binding.setVariable(BR.callback, callback)
        return SimpleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<B>, position: Int) {
        holder.binding.setVariable(modelVariableId, getItem(position))
        holder.binding.setVariable(BR.holder, holder)
        callback?.onViewBinding(holder, getItem(position)!!, position)
    }

    class SimpleViewHolder<S : ViewDataBinding>(val binding: S) : RecyclerView.ViewHolder(
        binding.root
    )
}