package com.attend.views.activities.home.ui.sections.section

import android.content.Intent
import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.ActivitySectionsBinding
import com.attend.views.activities.home.ui.account.SettingsActivity
import com.attend.views.activities.home.ui.users.admin.AdminsActivity

class SectionsActivity : BaseActivity() {

    private lateinit var binding: ActivitySectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.title = "Sections"

        loadFragment(SectionsFragment())
        bottomNav()

    }

    private fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun bottomNav() {
        binding.bottomNavigation.selectedItemId = R.id.item_sections
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_users -> {
                    startActivity(Intent(this@SectionsActivity, AdminsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.item_sections -> {
                    true
                }
                R.id.item_settings -> {
                    startActivity(Intent(this@SectionsActivity, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

}