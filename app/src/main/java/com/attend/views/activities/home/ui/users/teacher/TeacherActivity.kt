package com.attend.views.activities.home.ui.users.teacher

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.attend.BuildConfig
import com.attend.R
import com.attend.common.base.BaseActivity
import com.attend.common.util.PasswordValidator
import com.attend.data.models.users.Teacher
import com.attend.databinding.ActivityTeacherBinding

class TeacherActivity : BaseActivity() {

    private lateinit var binding: ActivityTeacherBinding
    private var teacherID: String? = ""
    private var teacher: Teacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Teacher"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        teacherID = intent.getStringExtra("teacherID") ?: ""

        binding.apply {
            if (teacherID!!.isNotEmpty()) {
                getById(teacherID!!)
                binding.checkboxPassword.visibility = View.VISIBLE
                binding.checkboxPassword.setOnCheckedChangeListener { _, isChecked ->
                    binding.inputPassword.visibility = if (isChecked) View.VISIBLE else View.GONE
                    binding.inputConfirmPassword.visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
                binding.inputPassword.visibility = View.GONE
                binding.inputConfirmPassword.visibility = View.GONE

            } else {
                binding.checkboxPassword.visibility = View.GONE
                binding.txtPassword.visibility = View.VISIBLE
                binding.inputConfirmPassword.visibility = View.VISIBLE
            }
        }

        binding.btnSave.setOnClickListener {
            if (container?.validator?.isEmpty(
                    binding.txtName, "Please enter your name"
                ) == true
                || container?.validator?.isEmpty(
                    binding.txtPhone,
                    "Please enter your phone"
                ) == true
                || container?.validator?.isPhoneNotValid(
                    binding.txtPhone,
                    "Please enter valid phone"
                ) == true
            ) return@setOnClickListener

            val name: String = binding.txtName.text.toString()
            val phone: String = binding.txtPhone.text.toString()
            val password: String = binding.txtPassword.text.toString()

            if(binding.checkboxPassword.isChecked || teacherID == ""){
                if (container?.validator?.isEmpty(binding.txtPassword, "Please enter your password") == true)
                    return@setOnClickListener

                if (!PasswordValidator.isValid(password)) {
                    binding.txtPassword.error = "Password must be 8 characters and contain Capital and Small letters, Numbers and Special Characters!"
                    binding.txtPassword.requestFocus()
                    return@setOnClickListener
                }

                if (container?.validator?.isNotEqual(binding.txtConfirmPassword, password,"Password not equals.") == true
                ) return@setOnClickListener
            }

            if (teacher != null) {
                teacher?.name = name
                teacher?.phone = phone
                if (binding.checkboxPassword.isChecked)
                    teacher?.password = password

                container?.repository?.getTeacherByPhone(teacher?.phone!!) {
                    if (it != null) {
                        if (it.phone == teacher?.phone) {
                            updateTeacher(teacher!!)
                        } else {
                            binding.txtPhone.error = "This Phone number already exists."
                            binding.txtPhone.requestFocus()
                            teacher = null
                        }
                    } else
                        updateTeacher(teacher!!)
                }
            } else {
                teacher = Teacher(
                    name = name,
                    phone = phone,
                    password = password
                )
                getTeacherByPhone(teacher!!)
            }
        }

        container?.up?.showToastMsg(
            this,
            "$teacherID --- ${intent.getBooleanExtra("updatedAccount", false)}"
        )
        if (!(intent.getBooleanExtra("updatedAccount", false) && teacherID != "")) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        deleteTeacher(teacherID!!)
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    }.show()
            }
        }
    }

    private fun getTeacherByPhone(data: Teacher) {
        container?.repository?.getTeacherByPhone(data.phone) {
            if (it != null) {
                binding.txtPhone.error = "This Phone number already exists."
                binding.txtPhone.requestFocus()
                teacher = null
            } else
                insertTeacher(data)
        }
    }

    private fun insertTeacher(data: Teacher) {
        container?.repository?.insertTeacher(data) {
            data.id = it
            updateTeacher(data)
        }
    }

    private fun updateTeacher(data: Teacher) {
        container?.repository?.updateTeacher(data) { isSaved ->
            if (isSaved) {
                close("Saved successfully")
            }
        }
    }

    private fun deleteTeacher(id: String) {
        container?.repository?.deleteTeacher(id) { isDeleted ->
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
        container?.repository?.getTeacher(id) {
            teacher = it
            setData()
        }
    }

    private fun setData() {
        supportActionBar?.title = "Edit Account"
        teacher?.let {
            binding.txtName.setText(it.name)
            binding.txtPhone.setText(it.phone)
        }
    }
}