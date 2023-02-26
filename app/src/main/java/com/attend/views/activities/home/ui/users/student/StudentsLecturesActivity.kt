package com.attend.views.activities.home.ui.users.student

import android.os.Bundle
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.ActivityStudentSubjectLecturesBinding
import com.attend.views.activities.home.ui.sections.lecture.AttendedLecturesFragment
import com.attend.views.activities.home.ui.sections.lecture.LecturesFragment
import com.google.android.material.tabs.TabLayout

class StudentsLecturesActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentSubjectLecturesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentSubjectLecturesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Lectures"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            loadFragment(LecturesFragment())
            /*if(container?.up?.student != null) {
                secondTab.visibility = View.GONE
                thirdTab.visibility = View.VISIBLE
            }
            else thirdTab.visibility = View.GONE*/
            subjectsTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Handle tab select
                    when (tab?.text.toString()) {
                        getString(R.string.all_lectures) -> {
                            loadFragment(LecturesFragment())
                        }
                        getString(R.string.attended_Lectures) -> {
                            loadFragment(AttendedLecturesFragment())
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