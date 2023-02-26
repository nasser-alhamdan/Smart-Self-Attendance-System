package com.attend.views.activities.home.ui.users.teacher

import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.ActivityTeachersBinding

class TeachersActivity : BaseActivity() {

    private lateinit var binding: ActivityTeachersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeachersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(TeacherLecturesFragment())
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
                    loadFragment(TeacherLecturesFragment())
                    true
                }
                R.id.item_subjects -> {
                    loadFragment(TeacherSubjectsFragment())
                    true
                }
                R.id.item_settings -> {
                    loadFragment(TeacherSettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

}