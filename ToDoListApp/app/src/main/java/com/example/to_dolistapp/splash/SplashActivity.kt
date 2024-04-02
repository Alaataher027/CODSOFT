package com.example.to_dolistapp.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.to_dolistapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY = 2000 // 2 seconds
    private lateinit var viewBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToHome()
        }, SPLASH_DELAY.toLong())

    }

    private fun navigateToHome() {
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)
        finish()
    }

}