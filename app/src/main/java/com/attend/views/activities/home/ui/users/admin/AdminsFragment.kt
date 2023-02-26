package com.attend.views.activities.home.ui.users.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.FragmentAdminsBinding

class AdminsFragment : BaseFragment() {

    private var _binding: FragmentAdminsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentAdminsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var adapter: AdminAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Admins"

        binding.apply {
            fab.setOnClickListener {
                startActivity(Intent(requireContext(), AdminActivity::class.java))
            }

            adapter = AdminAdapter {
                startActivity(
                    Intent(requireContext(), AdminActivity::class.java)
                        .putExtra("adminID", it.id)
                        .putExtra("updatedAccount", true)
                )
            }

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = "Users"
        getAdmins()
    }

    private fun getAdmins() {
        container?.repository?.getAdmins {
            adapter.submitList(it)
        }
    }
}