package com.attend.views.activities.home.ui.users.student

import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.ActivityStudentsBinding

class StudentsActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(StudentLecturesFragment())
        bottomNav()

    }

    private fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun bottomNav() {
        binding.bottomNavigation.selectedItemId = R.id.item_users
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_lectues -> {
                    loadFragment(StudentLecturesFragment())
                    true
                }
                R.id.item_subjects -> {
                    loadFragment(StudentSubjectsFragment())
                    true
                }
                R.id.item_settings -> {
                    loadFragment(StudentSettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

}