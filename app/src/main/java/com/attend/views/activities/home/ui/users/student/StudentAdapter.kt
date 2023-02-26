package com.attend.views.activities.home.ui.users.student

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attend.common.util.Utility
import com.attend.data.models.users.Student
import com.attend.databinding.ItemAdminBinding
import java.util.*

class StudentAdapter(private val onClick: (Student) -> Unit) :
    ListAdapter<Student, StudentAdapter.ViewHolder>(StudentDiffCallback) {

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

        fun bind(item: Student) {
            binding.txtName.text = String.format(Locale.ENGLISH, "%s\n%s", item.univID, item.name)
            if (item.timestamp > 0) {
                binding.txtPhone.text = String.format(
                    Locale.ENGLISH,
                    "%s\n%s",
                    item.phone,
                    Utility.convertTimestampToString(
                        item.timestamp,
                        "EE, dd MMM, yyyy - hh:mm a"
                    )
                )
            } else {
                binding.txtPhone.text = item.phone
            }
        }
    }
}

object StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.name == newItem.name
                && oldItem.phone == newItem.phone
                && oldItem.univID == newItem.univID
                && oldItem.password == newItem.password
    }
}
