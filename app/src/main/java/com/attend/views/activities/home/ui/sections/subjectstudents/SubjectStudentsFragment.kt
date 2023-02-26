package com.attend.views.activities.home.ui.sections.subjectstudents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.data.models.sections.Subject
import com.attend.data.models.sections.SubjectStudent
import com.attend.databinding.FragmentSubjectStudentsBinding
import com.attend.views.activities.home.ui.users.student.StudentsDialog

class SubjectStudentsFragment : BaseFragment() {

    private var _binding: FragmentSubjectStudentsBinding? = null
    private val binding get() = _binding!!
    private var subjectID: String = ""
    private var subject: Subject? = null
    private var subjectStudent: SubjectStudent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSubjectStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private lateinit var adapter: SubjectStudentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subjectID = (activity as AppCompatActivity).intent.getStringExtra("subjectID").toString()

        binding.apply {
            if (container?.up?.student == null) {
                fab.setOnClickListener {
                    StudentsDialog.view(requireContext()) {
                        getStudent(
                            SubjectStudent(
                                subject = subject,
                                subjectID = subject?.id ?: "",
                                student = it,
                                studentID = it.id
                            )
                        )
                    }
                }
            } else fab.visibility = View.GONE


            adapter = SubjectStudentsAdapter(
                canDelete = container?.up?.student == null,
                onClick = {
                    deleteSubjectStudent(it.id)
                })

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    private fun deleteSubjectStudent(id: String) {
        container?.repository?.deleteSubjectStudent(id) {
            if (it) {
                container?.up?.showToastMsg(requireContext(), "Deleted")
                getSubjectStudents(subjectID)
            }
        }
    }

    private fun getStudent(subjectStudent: SubjectStudent) {
        container?.repository?.checkSubjectStudentExist(
            subjectStudent.studentID,
            subjectStudent.subjectID
        ) {
            if (it != null) {
                container?.up?.showToastMsg(requireContext(), "Student is already in the list")
            } else {
                insertSubjectStudent(subjectStudent)
            }
        }
    }


    private fun insertSubjectStudent(subjectStudent: SubjectStudent) {
        container?.repository?.insertSubjectStudent(subjectStudent) {
            if (it != null) {
                subjectStudent.id = it
                updateSubjectStudent(subjectStudent)
            }
        }
    }

    private fun updateSubjectStudent(subjectStudent: SubjectStudent) {
        container?.repository?.updateSubjectStudent(subjectStudent) { isSaved ->
            if (isSaved) {
                getSubjectStudents(subjectID)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        getSubject(subjectID)
        getSubjectStudents(subjectID)
    }

    private fun getSubject(subjectID: String) {
        container?.repository?.getSubject(subjectID) {
            (activity as AppCompatActivity).supportActionBar?.title = it?.name
            subject = it
        }
    }

    private fun getSubjectStudents(subjectID: String) {
        container?.repository?.getSubjectStudents(subjectID = subjectID) {
            adapter.submitList(it)
        }
    }
}