package com.attend.views.activities.home.ui.users.teacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attend.data.models.users.Teacher
import com.attend.databinding.ItemAdminBinding

class TeacherAdapter(private val onClick: (Teacher) -> Unit) :
    ListAdapter<Teacher, TeacherAdapter.ViewHolder>(TeacherDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemAdminBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnBack.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bind(item: Teacher) {
            binding.txtName.text = item.name
            binding.txtPhone.text = item.phone
        }
    }
}

object TeacherDiffCallback : DiffUtil.ItemCallback<Teacher>() {
    override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
        return oldItem.name == newItem.name
                && oldItem.phone == newItem.phone
                && oldItem.password == newItem.password
    }
}
