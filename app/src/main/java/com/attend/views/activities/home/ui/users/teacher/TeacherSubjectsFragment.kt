package com.attend.views.activities.home.ui.users.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.FragmentTeacherSubjectsBinding
import com.attend.views.activities.home.ui.sections.lecture.LecturesActivity
import com.attend.views.activities.home.ui.sections.subject.SubjectAdapter

class TeacherSubjectsFragment : BaseFragment() {

    private var _binding: FragmentTeacherSubjectsBinding? = null
    private val binding get() = _binding!!
    private var sectionID: String = ""
    private var teacherID: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentTeacherSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private lateinit var adapter: SubjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Subjects"
        sectionID = (activity as AppCompatActivity).intent.getStringExtra("sectionID").toString()
        teacherID = container?.up?.teacher?.id

        binding.apply {
            adapter = SubjectAdapter(canEdit = false,
                onClick = {
                    startActivity(
                        Intent(requireContext(), LecturesActivity::class.java)
                            .putExtra("subjectID", it.id)
                            .putExtra("sectionID", sectionID)
                            .putExtra("updatedAccount", true)
                    )
                },
                onEdit = {}
            )

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    override fun onStart() {
        super.onStart()
        getSection(sectionID)
        getTeacherSubjects(teacherID)
    }

    private fun getSection(sectionID: String) {
        container?.repository?.getSection(sectionID) {
            (activity as AppCompatActivity).supportActionBar?.title = it?.name
        }
    }

    private fun getTeacherSubjects(teacherID: String?) {
        container?.repository?.getSubjects(teacherID = teacherID) {
            adapter.submitList(it)
        }
    }
}