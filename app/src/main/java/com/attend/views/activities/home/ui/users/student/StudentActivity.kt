package com.attend.views.activities.home.ui.users.student

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
import com.attend.data.models.users.Student
import com.attend.databinding.ActivityStudentBinding

class StudentActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentBinding
    private var studentID: String? = ""
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Student"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        studentID = intent.getStringExtra("studentID") ?: ""

        binding.apply {
            if (studentID!!.isNotEmpty()) {
                getById(studentID!!)
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
                    binding.txtUnivId,
                    "Please enter the University ID"
                ) == true
                || container?.validator?.isEmpty(
                    binding.txtName,
                    "Please enter your name"
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

            val univID: String = binding.txtUnivId.text.toString()
            val name: String = binding.txtName.text.toString()
            val phone: String = binding.txtPhone.text.toString()
            val password: String = binding.txtPassword.text.toString()


            if(binding.checkboxPassword.isChecked || studentID == ""){
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

            if (student != null) {
                student?.univID = univID
                student?.name = name
                student?.phone = phone
                if (binding.checkboxPassword.isChecked)
                    student?.password = password

                container?.repository?.getStudentByPhone(student?.phone!!) {
                    if (it != null) {
                        if (it.phone == student?.phone) {
                            updateStudent(student!!)
                        } else {
                            binding.txtPhone.error = "This Phone number already exists."
                            binding.txtPhone.requestFocus()
                            student = null
                        }
                    } else
                        updateStudent(student!!)
                }
            } else {
                student = Student(
                    univID = univID,
                    name = name,
                    phone = phone,
                    password = password
                )
                getStudentByPhone(student!!)
            }
        }

        container?.up?.showToastMsg(
            this,
            "$studentID --- ${intent.getBooleanExtra("updatedAccount", false)}"
        )
        if (!(intent.getBooleanExtra("updatedAccount", false) && studentID != "")) {
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        deleteStudent(studentID!!)
                    }
                    .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    }.show()
            }
        }
    }

    private fun getStudentByPhone(data: Student) {
        container?.repository?.getStudentByPhone(data.phone) {
            if (it != null) {
                binding.txtPhone.error = "This Phone number already exists."
                binding.txtPhone.requestFocus()
                student = null
            } else
                insertStudent(data)
        }
    }

    private fun insertStudent(data: Student) {
        container?.repository?.insertStudent(data) {
            data.id = it
            updateStudent(data)
        }
    }

    private fun updateStudent(data: Student) {
        container?.repository?.updateStudent(data) { isSaved ->
            if (isSaved) {
                close("Saved successfully")
            }
        }
    }

    private fun deleteStudent(id: String) {
        container?.repository?.deleteStudent(id) { isDeleted ->
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
        container?.repository?.getStudent(id) {
            student = it
            setData()
        }
    }

    private fun setData() {
        supportActionBar?.title = "Edit Account"
        student?.let {
            binding.txtUnivId.setText(it.univID)
            binding.txtName.setText(it.name)
            binding.txtPhone.setText(it.phone)
        }
    }
}