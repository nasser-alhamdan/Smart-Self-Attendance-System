package com.attend.views.activities.home.ui.sections.subject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.attend.BuildConfig
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.data.models.sections.Section
import com.attend.data.models.sections.Subject
import com.attend.data.models.users.Teacher
import com.attend.databinding.ActivitySubjectBinding

class SubjectActivity : BaseActivity() {

    private lateinit var binding: ActivitySubjectBinding
    private var subjectID: String? = ""
    private var sectionID: String? = ""
    private var subject: Subject? = null
    private var section: Section? = null
    private var teacher: Teacher? = null
    private var teachers: ArrayList<Teacher> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Subject"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.spinnerTeachers.postDelayed({
            getTeachers {
                val arrayAdapter = ArrayAdapter(this, R.layout.teachers_dropdown_menu_item, it)
                binding.spinnerTeachers.adapter = arrayAdapter
            }
        }, 1000)

        binding.spinnerTeachers.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    id: Long
                ) {
                    teacher = Teacher(
                        teachers[i].id, teachers[i].name, teachers[i].phone, teachers[i].password
                    )
                }
            }

        subjectID = intent.getStringExtra("subjectID") ?: ""
        sectionID = intent.getStringExtra("sectionID") ?: ""

        getSectionById(sectionID!!)

        binding.apply {
            if (subjectID!!.isNotEmpty()) {
                getById(subjectID!!)
            }
        }

        binding.btnSave.setOnClickListener {
            if (container?.validator?.isEmpty(
                    binding.txtName,
                    "Please enter Subject name"
                ) == true
                || container?.validator?.isEmpty(
                    binding.txtCode,
                    "Please enter Subject Code"
                ) == true
                || teacher == null
            ) return@setOnClickListener

            val name: String = binding.txtName.text.toString()
            val code: String = binding.txtCode.text.toString()
            val teacher: Teacher? = teacher

            if (subject != null) {
                subject?.name = name
                subject?.code = code
                subject?.teacher = teacher!!
                subject?.teacherID = teacher.id
                subject?.section = section!!
                subject?.sectionID = section?.id!!
                updateSubject(subject!!)
            } else {
                subject = Subject(
                    name = name,
                    code = code,
                    teacher = teacher!!,
                    teacherID = teacher.id,
                    section = section!!,
                    sectionID = section?.id!!
                )
                getSubjectByCode(subject!!)
            }
        }

        if (!(intent.getBooleanExtra("updatedAccount", false) && subjectID != "")) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        deleteSubject(subjectID!!)
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    }.show()
            }
        }
    }

    private fun getTeachers(onNamesLoaded: (ArrayList<String>) -> Unit) {
        val teachersNames = ArrayList<String>()
        container?.repository?.getTeachers {
            teachers.addAll(it)
            for (i in it) {
                teachersNames.add(i.name)
            }
            onNamesLoaded(teachersNames)
        }
    }

    private fun getSubjectByCode(data: Subject) {
        container?.repository?.getSubjectByCode(data.code) {
            if (it != null) {
                binding.txtCode.error = "This Subject code already exists."
                binding.txtCode.requestFocus()
                subject = null
            } else
                insertSubject(data)
        }
    }

    private fun insertSubject(data: Subject) {
        container?.repository?.insertSubject(data) {
            data.id = it
            updateSubject(data)
        }
    }

    private fun updateSubject(data: Subject) {
        container?.repository?.updateSubject(data) { isSaved ->
            if (isSaved) {
                close("Saved successfully")
            }
        }
    }

    private fun deleteSubject(id: String) {
        container?.repository?.deleteSubject(id) { isDeleted ->
            if (isDeleted) {
                close("Deleted successfully")
            }
        }
    }

    private fun close(string: String) {
        container?.repository?.hideProgress()
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getById(id: String) {
        container?.repository?.getSubject(id) {
            subject = it
            setData()
        }
    }

    private fun getSectionById(id: String) {
        container?.repository?.getSection(id) {
            section = it
        }
    }

    private fun setData() {
        supportActionBar?.title = "Edit Subject"
        subject?.let {
            binding.txtName.setText(it.name)
            binding.txtCode.setText(it.code)
            teacher = it.teacher
        }
    }


}