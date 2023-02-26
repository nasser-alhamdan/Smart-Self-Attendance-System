package com.attend.views.activities.home.ui.sections.lecture

import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.ActivitySubjectLecturesBinding
import com.attend.views.activities.home.ui.sections.subjectstudents.SubjectStudentsFragment
import com.google.android.material.tabs.TabLayout

class LecturesActivity : BaseActivity() {

    private lateinit var binding: ActivitySubjectLecturesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectLecturesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Lectures"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            loadFragment(LecturesFragment())
            subjectsTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Handle tab select
                    when (tab?.text.toString()) {
                        getString(R.string.all_lectures) -> {
                            loadFragment(LecturesFragment())
                        }
                        getString(R.string.subject_students) -> {
                            loadFragment(SubjectStudentsFragment())
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

    }

    private fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.subject_container, fragment)
        transaction.commit()
    }
}