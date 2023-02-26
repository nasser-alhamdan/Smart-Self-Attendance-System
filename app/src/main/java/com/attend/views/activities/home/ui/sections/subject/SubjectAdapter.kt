package com.attend.views.activities.home.ui.sections.subject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attend.data.models.sections.Subject
import com.attend.databinding.ItemSectionBinding

class SubjectAdapter(
    private val canEdit: Boolean = false,
    private val onClick: (Subject) -> Unit,
    private val onEdit: (Subject) -> Unit
) :
    ListAdapter<Subject, SubjectAdapter.ViewHolder>(SubjectDiffCallback) {

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
            binding.btnBack.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
            binding.btnEdit.setOnClickListener {
                onEdit(getItem(adapterPosition))
            }
        }

        fun bind(item: Subject) {
            binding.txtName.text = item.name
            binding.txtCode.text = item.code

            if (canEdit)
                binding.btnEdit.visibility = View.VISIBLE
            else
                binding.btnEdit.visibility = View.GONE

        }
    }
}

object SubjectDiffCallback : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem.name == newItem.name
                && oldItem.code == newItem.code
                && oldItem.teacherID == newItem.teacherID
    }
}
