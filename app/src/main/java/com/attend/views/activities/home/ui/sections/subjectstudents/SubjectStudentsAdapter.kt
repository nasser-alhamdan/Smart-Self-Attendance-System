package com.attend.views.activities.home.ui.sections.subjectstudents

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attend.data.models.sections.SubjectStudent
import com.attend.databinding.ItemSectionBinding

class SubjectStudentsAdapter(
    private val canDelete: Boolean = false,
    private val onClick: (SubjectStudent) -> Unit
) :
    ListAdapter<SubjectStudent, SubjectStudentsAdapter.ViewHolder>(SubjectDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemSectionBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnEdit.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bind(item: SubjectStudent) {
            binding.txtName.text = item.student?.name
            binding.txtCode.text = item.student?.univID
            if (canDelete) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.text = String.format("Delete")
                binding.btnEdit.setBackgroundColor(Color.RED)
            }
        }
    }
}

object SubjectDiffCallback : DiffUtil.ItemCallback<SubjectStudent>() {
    override fun areItemsTheSame(oldItem: SubjectStudent, newItem: SubjectStudent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SubjectStudent, newItem: SubjectStudent): Boolean {
        return oldItem.student == newItem.student
                && oldItem.studentID == newItem.studentID
                && oldItem.subject == newItem.subject
                && oldItem.subjectID == newItem.subjectID
    }
}
