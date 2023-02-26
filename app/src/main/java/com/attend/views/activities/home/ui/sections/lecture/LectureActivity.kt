package com.attend.views.activities.home.ui.sections.lecture

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.util.Utility
import com.attend.data.models.sections.Lecture
import com.attend.data.models.sections.Subject
import com.attend.databinding.ActivityLectureBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LectureActivity : BaseActivity() {

    private lateinit var binding: ActivityLectureBinding
    private var lectureID: String? = ""
    private var subjectID: String? = ""
    private var lecture: Lecture? = null
    private var subject: Subject? = null
    private var c = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Lecture"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lectureID = intent.getStringExtra("lectureID") ?: ""
        subjectID = intent.getStringExtra("subjectID") ?: ""

        getSubjectById(subjectID!!)

        binding.apply {
            if (lectureID!!.isNotEmpty()) getById(lectureID!!)

            dateLyt.setEndIconOnClickListener { showDPD() }
            timeLyt.setEndIconOnClickListener { showTPD() }
        }

        binding.btnSave.setOnClickListener {
            if (container?.validator?.isEmpty(
                    binding.txtName, "Please enter Lecture name"
                ) == true || container?.validator?.isEmpty(
                    binding.time, "Please enter Lecture Date"
                ) == true || container?.validator?.isEmpty(
                    binding.date, "Please enter Lecture Start Time"
                ) == true
            ) return@setOnClickListener

            val title: String = binding.txtName.text.toString()
            val date: String = binding.date.text.toString()
            val time: String = binding.time.text.toString()

            if (lecture != null) {
                lecture?.title = title
                lecture?.date = date
                lecture?.time = time
                lecture?.timestamp = Date().time
                lecture?.subject = subject!!
                lecture?.subjectID = subject?.id!!
                updateLecture(lecture!!)
            } else {
                lecture = Lecture(
                    title = title,
                    date = date,
                    time = time,
                    timestamp = Date().time,
                    subject = subject!!,
                    subjectID = subject?.id!!,
                )
                insertLecture(lecture!!)
            }
        }

        if (!(intent.getBooleanExtra("updatedAccount", false) && lectureID != "")) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this).setTitle("Delete").setMessage("Are you sure?")
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        deleteLecture(lectureID!!)
                    }.setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    }.show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showTPD() {
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this@LectureActivity, { _, h, m ->
            var date = "$h:$m"

            val simpleDateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
            try {
                date = SimpleDateFormat(
                    "HH:mm", Locale.getDefault()
                ).format(simpleDateFormat.parse(date)!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            binding.time.setText(" $date ")
        }, hour, minute, true)
        tpd.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDPD() {
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this@LectureActivity, { _, y, m, d ->
            binding.date.setText(Utility.getDate(y, m, d))
        }, year, month, day)

        dpd.datePicker.minDate = Date().time
        dpd.show()
    }


    private fun insertLecture(data: Lecture) {
        container?.repository?.insertLecture(data) {
            data.id = it
            updateLecture(data)
        }
    }

    private fun updateLecture(data: Lecture) {
        container?.repository?.updateLecture(data) { isSaved ->
            if (isSaved) {
                close("Saved successfully")
            }
        }
    }

    private fun deleteLecture(id: String) {
        container?.repository?.deleteLecture(id) { isDeleted ->
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

    private fun getSubjectById(id: String) {
        container?.repository?.getSubject(id) {
            subject = it
        }
    }

    private fun getById(id: String) {
        container?.repository?.getLecture(id) {
            lecture = it
            setData()
        }
    }

    private fun setData() {
        supportActionBar?.title = "Edit Lecture"
        lecture?.let {
            binding.txtName.setText(it.title)
            binding.date.setText(it.date)
            binding.time.setText(it.time)
            subject = it.subject
        }
    }
}