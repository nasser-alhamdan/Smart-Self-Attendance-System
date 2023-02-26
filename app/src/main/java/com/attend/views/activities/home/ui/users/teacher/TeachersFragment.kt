package com.attend.views.activities.home.ui.users.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.FragmentTeachersBinding

class TeachersFragment : BaseFragment() {

    private var _binding: FragmentTeachersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentTeachersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var adapter: TeacherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Admins"

        binding.apply {
            fab.setOnClickListener {
                startActivity(Intent(requireContext(), TeacherActivity::class.java))
            }

            adapter = TeacherAdapter {
                startActivity(
                    Intent(requireContext(), TeacherActivity::class.java)
                        .putExtra("teacherID", it.id)
                        .putExtra("updatedAccount", true)
                )
            }

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    override fun onStart() {
        super.onStart()
        getTeachers()
    }

    private fun getTeachers() {
        container?.repository?.getTeachers {
            adapter.submitList(it)
        }
    }
}