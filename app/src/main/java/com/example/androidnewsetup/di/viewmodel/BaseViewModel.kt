package com.example.androidnewsetup.di.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.androidnewsetup.data.remote.ApiRepositoryImpl
import com.example.androidnewsetup.di.event.SingleLiveEvent
import com.example.androidnewsetup.di.MyApplication
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    @JvmField
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @JvmField
    val obrClick: SingleLiveEvent<View> = SingleLiveEvent()

    val apiRepoImpl: ApiRepositoryImpl? = MyApplication.instance?.apiRepoImpl

    fun onClick(view: View) {
        obrClick.value = view
    }

    //Get network message
    fun getNetworkMsg(e: Exception): String {
        return MyApplication.instance?.networkErrorHandler?.getErrMsg(e) ?: ""
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}