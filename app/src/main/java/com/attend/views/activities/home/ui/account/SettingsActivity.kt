package com.attend.views.activities.home.ui.account

import android.content.Intent
import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.data.models.users.Admin
import com.attend.databinding.ActivitySettingsBinding
import com.attend.views.activities.LaunchActivity
import com.attend.views.activities.home.ui.sections.section.SectionsActivity
import com.attend.views.activities.home.ui.users.admin.AdminActivity
import com.attend.views.activities.home.ui.users.admin.AdminsActivity

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var admin: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

        admin = container?.up?.admin!!


        bottomNav()

        binding.contentProfile.profile.setOnClickListener {
            startActivity(
                Intent(
                    this@SettingsActivity,
                    AdminActivity::class.java
                ).putExtra("adminID", admin.id)
            )
        }

        binding.txtProfileLogout.setOnClickListener { logout() }
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setData() {
        binding.contentProfile.txtProfileName.text = admin.name
        binding.contentProfile.txtProfilePhone.text = admin.phone
    }

    private fun logout() {
        container?.up?.logout()
        finish()
        startActivity(Intent(this, LaunchActivity::class.java))
    }

    private fun bottomNav() {
        binding.bottomNavigation.selectedItemId = R.id.item_settings
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_users -> {
                    startActivity(Intent(this@SettingsActivity, AdminsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.item_sections -> {
                    startActivity(Intent(this@SettingsActivity, SectionsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.item_settings -> {
                    true
                }
                else -> false
            }
        }
    }
}