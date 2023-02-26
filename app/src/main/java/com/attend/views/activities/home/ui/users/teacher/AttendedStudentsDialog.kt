package com.attend.views.activities.home.ui.users.teacher

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.attend.common.base.BaseBottomDialog
import com.attend.data.models.users.Student
import com.attend.databinding.FragmentStudentsBinding
import com.attend.views.activities.home.ui.users.student.StudentAdapter

class AttendedStudentsDialog : BaseBottomDialog() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private var onClick: ((Student) -> Unit)? = null
    private var lectureID: String? = null

    companion object {
        fun view(context: Context, lectureID: String, onClick: (Student) -> Unit) {
            val dialog = AttendedStudentsDialog()
            dialog.onClick = onClick
            dialog.lectureID = lectureID
            dialog.show((context as FragmentActivity).supportFragmentManager, null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var adapter: StudentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fab.visibility = View.GONE
            txtSearch.visibility = View.GONE
            dialogTitle.visibility = View.VISIBLE
            adapter = StudentAdapter {
                onClick?.let { it1 ->
                    it1(it)
                }
            }
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter
        }

    }

    override fun onStart() {
        super.onStart()
        getStudents()
    }

    private fun getStudents() {
        var attendedStudents: List<Student>
        container?.repository?.getLectureAttendances(lectureID = lectureID) {
            attendedStudents = it.map {
                val student = it.student
                student.timestamp = it.timestamp
                student
            }
            adapter.submitList(attendedStudents)
        }
    }
}