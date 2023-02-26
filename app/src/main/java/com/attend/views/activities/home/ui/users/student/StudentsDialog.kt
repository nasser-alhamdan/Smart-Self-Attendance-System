package com.attend.views.activities.home.ui.users.student

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.attend.R
import com.attend.common.base.BaseBottomDialog
import com.attend.data.models.users.Student
import com.attend.databinding.FragmentStudentsBinding


class StudentsDialog : BaseBottomDialog() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private var onClick: ((Student) -> Unit)? = null

    companion object {
        fun view(context: Context, onClick: (Student) -> Unit) {
            val dialog: StudentsDialog = StudentsDialog()
            dialog.onClick = onClick
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

    private lateinit var students: ArrayList<Student>
    private lateinit var adapter: StudentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fab.visibility = View.GONE
            dialogTitle.text = getString(R.string.students)

            students = ArrayList()

            adapter = StudentAdapter {
                onClick?.let { it1 ->
                    it1(it)
                    dismiss()
                }
            }
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

            txtSearch.editText?.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val studentsList = students.filter { it ->
                        if (s.isNotEmpty())
                            it.name.lowercase().contains(s.toString().lowercase())
                                    || it.univID.contains(s.toString())
                                    || it.phone.contains(s.toString())
                        else
                            true
                    }
                    adapter.submitList(studentsList)
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
                ) {

                }

                override fun afterTextChanged(s: Editable) {

                }
            })
        }

    }

    override fun onStart() {
        super.onStart()
        getStudents()
    }

    private fun getStudents() {
        container?.repository?.getStudents {
            students.addAll(it)
            adapter.submitList(students)
        }
    }
}