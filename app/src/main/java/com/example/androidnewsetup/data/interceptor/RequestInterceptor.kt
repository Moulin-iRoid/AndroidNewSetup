package com.example.androidnewsetup.data.interceptor

import android.util.Log
import com.example.androidnewsetup.di.MyApplication
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        runBlocking {
            try {
                val authData = MyApplication.instance?.getAuthData()
                authData?.accessToken?.let { token ->
                    builder.addHeader("Authorization", "Bearer $token")
                }
            } catch (e: Exception) {
                Log.e("Authorization Exception", "")
            }
        }

        return chain.proceed(builder.build())
    }
}