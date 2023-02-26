package com.attend.views.activities.home.ui.users.student

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.data.models.users.Student
import com.attend.databinding.FragmentStudentSettingsBinding
import com.attend.views.activities.LaunchActivity

class StudentSettingsFragment : BaseFragment() {

    private var _binding: FragmentStudentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var student: Student

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentStudentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Settings"

        student = container?.up?.student!!

        binding.contentProfile.profile.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(), StudentActivity::class.java
                ).putExtra("studentID", student.id)
            )

        }

        binding.txtProfileLogout.setOnClickListener { logout() }
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setData() {
        binding.contentProfile.txtProfileName.text = student.name
        binding.contentProfile.txtProfileType.text = String.format("Student ID: %s", student.univID)
        binding.contentProfile.txtProfilePhone.text = student.phone
    }

    private fun logout() {
        container?.up?.logout()
        (activity as AppCompatActivity).finish()
        startActivity(Intent(requireContext(), LaunchActivity::class.java))
    }
}