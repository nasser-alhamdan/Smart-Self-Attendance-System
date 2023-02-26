package com.attend.views.activities.home.ui.sections.section

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attend.data.models.sections.Section
import com.attend.databinding.ItemSectionBinding

class SectionAdapter(
    private val onEdit: (Section) -> Unit,
    private val onClick: (Section) -> Unit
) :
    ListAdapter<Section, SectionAdapter.ViewHolder>(SectionDiffCallback) {

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
                onEdit(getItem(adapterPosition))
            }
            binding.btnBack.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bind(item: Section) {
            binding.txtName.text = item.name
            binding.txtCode.text = item.code
        }
    }
}

object SectionDiffCallback : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem.name == newItem.name
                && oldItem.code == newItem.code
    }
}
