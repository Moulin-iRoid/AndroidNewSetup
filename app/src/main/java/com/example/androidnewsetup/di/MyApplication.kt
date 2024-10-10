package com.example.androidnewsetup.di

import android.app.Application
import android.content.Intent
import com.example.androidnewsetup.data.bean.user.Authentication
import com.example.androidnewsetup.data.remote.ApiInterface
import com.example.androidnewsetup.data.remote.ApiRepositoryImpl
import com.example.androidnewsetup.data.remote.RetrofitBuilder
import com.example.androidnewsetup.data.remote.helper.NetworkErrorHandler
import com.example.androidnewsetup.ui.welcome.splash.SplashActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testfairy.TestFairy
import com.example.androidnewsetup.util.UserManager

class MyApplication : Application() {

    //Network message
    var networkErrorHandler: NetworkErrorHandler? = null

    //Web services
    var apiRepoImpl: ApiRepositoryImpl? = null
    lateinit var userManager: UserManager

    override fun onCreate() {
        super.onCreate()
        instance = this

        userManager = UserManager(this)

        initWebServices()
        TestFairy.begin(this, "SDK-nDqC3mOd")
    }

    //Init Web Services and Network handler
    private fun initWebServices() {
        networkErrorHandler = NetworkErrorHandler(this)

        val apiInt = RetrofitBuilder.getRetrofit().create(ApiInterface::class.java)
        apiRepoImpl = ApiRepositoryImpl(apiInt)
    }

    suspend fun getAuthData(): Authentication? {
        val jsonString = userManager.getUserAuthJson()
        if (jsonString.isNullOrEmpty()) {
            return null
        }

        return Gson().fromJson(
            jsonString,
            object : TypeToken<Authentication>() {}.type
        )
    }

    //Restart App
    fun restartApp() {
        val intent: Intent = SplashActivity().newIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("reset", true)
        startActivity(intent)
    }

    companion object {
        var instance: MyApplication? = null
            private set
    }
}