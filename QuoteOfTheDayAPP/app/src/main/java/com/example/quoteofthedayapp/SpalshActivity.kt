package com.example.quoteofthedayapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.quoteofthedayapp.databinding.ActivitySpalshBinding

class SpalshActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 2500
    private lateinit var viewBinding: ActivitySpalshBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySpalshBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToHome()
        }, SPLASH_DELAY.toLong())
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}