package com.example.androidnewsetup.di.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.androidnewsetup.BR
import com.example.androidnewsetup.di.adapter.SimpleRecyclerViewAdapter.SimpleViewHolder
import java.util.*

class SimpleRecyclerViewAdapter<M, B : ViewDataBinding> :
    RecyclerView.Adapter<SimpleViewHolder<B>> {
    @LayoutRes
    private val layoutResId: Int
    private val modelVariableId: Int
    private var callback: SimpleCallback<M, B>? = null
    private val dataList: MutableList<M> = ArrayList()

    fun removeItem(i: Int) {
        try {
            if (i != -1) {
                dataList.removeAt(i)
                notifyItemRemoved(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    operator fun set(i: Int, scanResult: M) {
        if (scanResult == null) return
        dataList.add(i, scanResult)
        notifyItemChanged(i)
    }

    interface SimpleCallback<M, B : ViewDataBinding> {
        fun onItemClick(v: View, m: M) {}
        fun onItemClick(v: View, m: M, pos: Int) {}
        fun onItemClick(v: View, m: M, pos: Int,holder: SimpleViewHolder<B>?) {}
        fun onPositionClick(v: View, pos: Int) {}
        fun onViewBinding(holder: SimpleViewHolder<B>, m: M, pos: Int) {}

    }

    constructor(@LayoutRes layoutResId: Int, modelVariableId: Int, callback: SimpleCallback<M, B>) {
        this.layoutResId = layoutResId
        this.modelVariableId = modelVariableId
        this.callback = callback
    }

    constructor(@LayoutRes layoutResId: Int, modelVariableId: Int) {
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
        holder.binding.setVariable(modelVariableId, dataList[position])
        holder.binding.setVariable(BR.holder, holder)
        callback?.onViewBinding(holder, dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDummyList(size: Int, seed: M) {
        dataList.clear()
        var i = 0
        while (i < size) {
            dataList.add(seed)
            i++
        }
        notifyDataSetChanged()
    }

    var list: List<M>
        get() = dataList
        @SuppressLint("NotifyDataSetChanged")
        set(newDataList) {
            dataList.clear()
            dataList.addAll(newDataList)
            notifyDataSetChanged()
        }

    fun addToList(newDataList: List<M>) {
        val positionStart = dataList.size
        val itemCount = newDataList.size
        dataList.addAll(newDataList)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addData(data: M) {
        val positionStart = dataList.size
        dataList.add(data)
        notifyItemInserted(positionStart)
    }

    fun addData(position: Int, data: M) {
        val positionStart = dataList.size
        dataList.add(position, data)
        notifyItemInserted(positionStart)
    }


    class SimpleViewHolder<S : ViewDataBinding>(val binding: S) : RecyclerView.ViewHolder(
        binding.root
    )


}