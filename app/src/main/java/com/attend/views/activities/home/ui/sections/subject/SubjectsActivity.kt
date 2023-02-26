package com.attend.views.activities.home.ui.sections.subject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.attend.common.base.BaseActivity
import com.attend.databinding.ActivitySubjectsBinding
import com.attend.views.activities.home.ui.sections.lecture.LecturesActivity

class SubjectsActivity : BaseActivity() {

    private lateinit var binding: ActivitySubjectsBinding
    private var sectionID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onViewCreated()
    }


    private lateinit var adapter: SubjectAdapter

    private fun onViewCreated() {

        binding.apply {
            fab.setOnClickListener {
                startActivity(
                    Intent(this@SubjectsActivity, SubjectActivity::class.java)
                        .putExtra("sectionID", sectionID)
                )
            }

            adapter = SubjectAdapter(canEdit = true,
                onClick = {
                    startActivity(
                        Intent(this@SubjectsActivity, LecturesActivity::class.java)
                            .putExtra("subjectID", it.id)
                            .putExtra("sectionID", sectionID)
                            .putExtra("updatedAccount", true)
                    )
                }, onEdit = {
                    startActivity(
                        Intent(this@SubjectsActivity, SubjectActivity::class.java)
                            .putExtra("subjectID", it.id)
                            .putExtra("sectionID", sectionID)
                            .putExtra("updatedAccount", true)
                    )
                }
            )

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    override fun onStart() {
        super.onStart()
        sectionID = intent.getStringExtra("sectionID").toString()
        getSection(sectionID)
        getSubjects()
    }

    private fun getSection(sectionID: String) {
        container?.repository?.getSection(sectionID) {
            supportActionBar?.title = it?.name
        }
    }

    private fun getSubjects() {
        container?.repository?.getSubjects(sectionID = sectionID) {
            adapter.submitList(it)
        }
    }
}