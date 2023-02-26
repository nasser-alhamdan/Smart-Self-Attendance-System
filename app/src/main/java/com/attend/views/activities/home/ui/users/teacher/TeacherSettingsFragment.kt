package com.attend.views.activities.home.ui.users.teacher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.data.models.users.Teacher
import com.attend.databinding.FragmentTeacherSettingsBinding
import com.attend.views.activities.LaunchActivity

class TeacherSettingsFragment : BaseFragment() {

    private var _binding: FragmentTeacherSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var teacher: Teacher

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentTeacherSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Settings"

        teacher = container?.up?.teacher!!

        binding.contentProfile.profile.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    TeacherActivity::class.java
                ).putExtra("teacherID", teacher.id)
            )

        }

        binding.txtProfileLogout.setOnClickListener { logout() }
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun setData() {
        binding.contentProfile.txtProfileName.text = teacher.name
        binding.contentProfile.txtProfileType.text = String.format("Teacher")
        binding.contentProfile.txtProfilePhone.text = teacher.phone
    }

    private fun logout() {
        container?.up?.logout()
        (activity as AppCompatActivity).finish()
        startActivity(Intent(requireContext(), LaunchActivity::class.java))
    }
}