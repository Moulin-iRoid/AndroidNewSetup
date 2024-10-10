package com.example.androidnewsetup.ui.welcome.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.androidnewsetup.databinding.ActivitySplashBinding
import com.example.androidnewsetup.di.view.AppActivity
import com.example.androidnewsetup.ui.main.MainActivity
import com.example.androidnewsetup.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val vm: SplashActivityVM by viewModels()

    fun newIntent(context: Context): Intent {
        val intent = Intent(context, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = vm

        appDataManager = UserManager(this)

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            if (appDataManager?.getUserData() != null) {
                startNewActivity(MainActivity().newIntent(this@SplashActivity), true, animate = true)
            } else {
                startNewActivity(MainActivity().newIntent(this@SplashActivity), true, animate = true)
            }
        }
    }
}