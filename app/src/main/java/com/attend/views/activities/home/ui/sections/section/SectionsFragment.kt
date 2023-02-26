package com.attend.views.activities.home.ui.sections.section

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.common.base.BaseFragment
import com.attend.databinding.FragmentAdminsBinding
import com.attend.views.activities.home.ui.sections.subject.SubjectsActivity

class SectionsFragment : BaseFragment() {

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

    private lateinit var adapter: SectionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fab.setOnClickListener {
                startActivity(Intent(requireContext(), SectionActivity::class.java))
            }

            adapter = SectionAdapter(
                onEdit = {
                    startActivity(
                        Intent(requireContext(), SectionActivity::class.java)
                            .putExtra("sectionID", it.id)
                            .putExtra("updatedAccount", true)
                    )
                },
                onClick = {
                    startActivity(
                        Intent(requireContext(), SubjectsActivity::class.java)
                            .putExtra("sectionID", it.id)
                            .putExtra("section", it)
                    )
                })

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = "Sections"
        getSections()
    }

    private fun getSections() {
        container?.repository?.getSections {
            adapter.submitList(it)
        }
    }
}