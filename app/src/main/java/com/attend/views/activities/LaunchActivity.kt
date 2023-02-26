package com.attend.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.attend.common.base.BaseActivity
import com.attend.common.util.UserTypes
import com.attend.databinding.ActivityLaunchBinding
import com.attend.views.activities.auth.LoginActivity
import com.attend.views.activities.home.ui.users.admin.AdminsActivity
import com.attend.views.activities.home.ui.users.student.StudentsActivity
import com.attend.views.activities.home.ui.users.teacher.TeachersActivity

class LaunchActivity : BaseActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (intent.getBooleanExtra("launch", true)) {
            binding.imageView.postDelayed({
                if (container?.up?.checkUserState() == UserTypes.ADMIN) {
                    navigateToAdminMain()
                } else if (container?.up?.checkUserState() == UserTypes.TEACHER) {
                    navigateToTeacherMain()
                } else if (container?.up?.checkUserState() == UserTypes.STUDENT) {
                    navigateToStudentMain()
                } else binding.signInOptions.visibility = View.VISIBLE
            }, 2000)

            binding.btnAdmin.setOnClickListener { login(UserTypes.ADMIN) }

            binding.btnTeacher.setOnClickListener { login(UserTypes.TEACHER) }

            binding.btnStudent.setOnClickListener { login(UserTypes.STUDENT) }
        }
    }

    private fun login(type: String) {
        startActivity(Intent(this, LoginActivity::class.java).putExtra("type", type))
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