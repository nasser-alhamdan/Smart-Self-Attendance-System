package com.attend.views.activities.home.ui.users.admin

import android.content.Intent
import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.ActivityAdminsBinding
import com.attend.views.activities.home.ui.account.SettingsActivity
import com.attend.views.activities.home.ui.sections.section.SectionsActivity
import com.attend.views.activities.home.ui.users.student.StudentsFragment
import com.attend.views.activities.home.ui.users.teacher.TeachersFragment
import com.google.android.material.tabs.TabLayout

class AdminsActivity : BaseActivity() {

    private lateinit var binding: ActivityAdminsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Users"

        binding.apply {
            loadFragment(AdminsFragment())
            tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Handle tab select
                    when (tab?.text.toString()) {
                        getString(R.string.admins) -> {
                            loadFragment(AdminsFragment())
                        }
                        getString(R.string.teachers) -> {
                            loadFragment(TeachersFragment())
                        }
                        getString(R.string.students) -> {
                            loadFragment(StudentsFragment())
                        }
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Handle tab reselect
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Handle tab unselect
                }
            })

        }
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
                R.id.item_users -> {
                    true
                }
                R.id.item_sections -> {
                    startActivity(Intent(this@AdminsActivity, SectionsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.item_settings -> {
                    startActivity(Intent(this@AdminsActivity, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

}