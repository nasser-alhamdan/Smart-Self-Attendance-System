package com.attend.views.activities.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.attend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.title = "Teachers"

//        bottomNav()
    }

/*    private fun bottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_users -> {
                    true
                }
                R.id.item_sections -> {
                    true
                }
                R.id.item_settings -> {
                    true
                }
                else -> false
            }
        }
    }*/
}