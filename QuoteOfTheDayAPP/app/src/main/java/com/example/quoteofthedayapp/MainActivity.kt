package com.example.quoteofthedayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.quoteofthedayapp.databinding.ActivityMainBinding
import com.example.quoteofthedayapp.favorite.FavFragment
import com.example.quoteofthedayapp.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val HomeFragment = HomeFragment()
        val FavFragment = FavFragment()

        setCurrentFragment(HomeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setCurrentFragment(HomeFragment)
                R.id.navigation_favorite -> setCurrentFragment(FavFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

}