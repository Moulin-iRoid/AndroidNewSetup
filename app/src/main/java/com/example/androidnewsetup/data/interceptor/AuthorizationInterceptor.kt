package com.example.androidnewsetup.data.interceptor

import com.example.androidnewsetup.di.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val mainResponse = chain.proceed(chain.request())
        if (mainResponse.code == 403) {
            MyApplication.instance?.restartApp()
        }
        return mainResponse
    }
}