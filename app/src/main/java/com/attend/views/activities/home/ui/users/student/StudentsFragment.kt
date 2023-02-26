package com.attend.views.activities.home.ui.users.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.FragmentStudentsBinding

class StudentsFragment : BaseFragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!

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

        (activity as AppCompatActivity).supportActionBar?.title = "Admins"

        binding.apply {
            fab.setOnClickListener {
                startActivity(Intent(requireContext(), StudentActivity::class.java))
            }

            if (container?.up?.admin != null) {
                dialogTitle.visibility = View.GONE
                txtSearch.visibility = View.GONE
            }

            adapter = StudentAdapter {
                startActivity(
                    Intent(requireContext(), StudentActivity::class.java)
                        .putExtra("studentID", it.id)
                        .putExtra("updatedAccount", true)
                )
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
        container?.repository?.getStudents {
            adapter.submitList(it)
        }
    }
}