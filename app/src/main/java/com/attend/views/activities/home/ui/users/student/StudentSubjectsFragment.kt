package com.attend.views.activities.home.ui.users.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.data.models.sections.Subject
import com.attend.databinding.FragmentStudentSubjectsBinding
import com.attend.views.activities.home.ui.sections.subject.SubjectAdapter

class StudentSubjectsFragment : BaseFragment() {

    private var _binding: FragmentStudentSubjectsBinding? = null
    private val binding get() = _binding!!
    private var sectionID: String = ""
    private var studentID: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentStudentSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private lateinit var adapter: SubjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Registered Subjects"
        sectionID = (activity as AppCompatActivity).intent.getStringExtra("sectionID").toString()
        studentID = container?.up?.student?.id

        binding.apply {
            adapter = SubjectAdapter(canEdit = false,
                onClick = {
                    startActivity(
                        Intent(
                            requireContext(),
                            StudentsLecturesActivity::class.java
                        )
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
        getSubjectStudents(studentID)
    }


    private fun getSubjectStudents(studentID: String?) {
        container?.repository?.getSubjectStudents(studentID = studentID) {
            val studentSubjects: ArrayList<Subject> = ArrayList()
            it.forEach { item ->
                studentSubjects.add(item.subject!!)
            }
            adapter.submitList(studentSubjects)
        }
    }
}