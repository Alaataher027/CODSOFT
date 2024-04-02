package com.example.to_dolistapp.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.to_dolistapp.mainActivity.MainActivity
import com.example.to_dolistapp.R
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage

class SplashScreen : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fragmentManager = supportFragmentManager
        sharedPreferences = getSharedPreferences("SplashScreen", Context.MODE_PRIVATE)

        if (!isFirstTimeLaunch()) {
            navigateToMainActivity()
            return
        }

        val paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataforOnboarding())
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_layout, paperOnboardingFragment)
        fragmentTransaction.commit()

        paperOnboardingFragment.setOnRightOutListener {
            markFirstTimeLaunch()
            navigateToMainActivity()
        }
    }

    private fun isFirstTimeLaunch(): Boolean {
        return sharedPreferences.getBoolean("isFirstTimeLaunch", true)
    }

    private fun markFirstTimeLaunch() {
        sharedPreferences.edit().putBoolean("isFirstTimeLaunch", false).apply()
    }

    private fun getDataforOnboarding(): ArrayList<PaperOnboardingPage> {
        val source = PaperOnboardingPage(
            "Manage Your \n Everyday Task List",
            "",
            Color.parseColor("#FFFFFF"),
            R.drawable.splash_1,
            R.drawable.circle
        )
        val source1 = PaperOnboardingPage(
            "Make It Easier!",
            "",
            Color.parseColor("#FFFFFF"),
            R.drawable.splash2,
            R.drawable.circle
        )
        val source2 = PaperOnboardingPage(
            "Let's Start!",
            " ",
            Color.parseColor("#FFFFFF"),
            R.drawable.splash3,
            R.drawable.start
        )

        val elements = ArrayList<PaperOnboardingPage>()
        elements.add(source)
        elements.add(source1)
        elements.add(source2)
        return elements
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
