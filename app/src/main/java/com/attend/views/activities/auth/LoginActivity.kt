package com.attend.views.activities.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.attend.BuildConfig
import com.attend.common.base.BaseActivity
import com.attend.common.util.UserTypes
import com.attend.common.util.Utility
import com.attend.databinding.ActivityLoginBinding
import com.attend.views.activities.home.MainActivity
import com.attend.views.activities.home.ui.users.admin.AdminsActivity
import com.attend.views.activities.home.ui.users.student.StudentsActivity
import com.attend.views.activities.home.ui.users.teacher.TeachersActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val type = intent.getStringExtra("type")

        binding.btnLogin.setOnClickListener {
            if (container?.validator?.isEmpty(
                    binding.phoneInput.editText!!, "Please enter your phone"
                ) == true
                || container?.validator?.isPhoneNotValid(
                    binding.phoneInput.editText!!, "Please enter valid phone"
                ) == true
                || container?.validator?.isEmpty(
                    binding.passwordInput.editText!!, "Please enter your password"
                ) == true
            ) return@setOnClickListener

            val phone: String = binding.phoneInput.editText?.text.toString()
            val password: String = binding.passwordInput.editText?.text.toString()

            when (type) {
                UserTypes.ADMIN -> adminLogin(phone, password)
                UserTypes.TEACHER -> teacherLogin(phone, password)
                UserTypes.STUDENT -> studentLogin(phone, password)
            }

        }

    }

    private fun adminLogin(phone: String, password: String) {
        container?.repository?.authAdminLogin(phone, password) {
            if (it != null) {
                container?.up?.saveUser(it)
                navigateToAdminMain()
            } else {
                Utility.showMsg("Wrong Phone or Password ,please try again.")
            }
        }
    }

    private fun teacherLogin(phone: String, password: String) {
        container?.repository?.authTeacherLogin(phone, password) {
            if (it != null) {
                container?.up?.saveUser(it)
                navigateToTeacherMain()
            } else {
                Utility.showMsg("Wrong Phone or Password ,please try again.")
            }
        }
    }

    private fun studentLogin(phone: String, password: String) {
        container?.repository?.authStudentLogin(phone, password) {
            if (it != null) {
                container?.up?.saveUser(it)
                navigateToStudentMain()
            } else {
                Utility.showMsg("Wrong Phone or Password ,please try again.")
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun navigateToAdminMain() {
        val intent = Intent(this, AdminsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun navigateToStudentMain() {
        val intent = Intent(this, StudentsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun navigateToTeacherMain() {
        val intent = Intent(this, TeachersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}