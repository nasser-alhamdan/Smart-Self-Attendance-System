package com.attend.views.activities.home.ui.sections.lecture

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attend.R
import com.attend.common.util.Utility
import com.attend.data.models.sections.Lecture
import com.attend.databinding.ItemLectureBinding
import java.util.*

class LectureAdapter(
    private val showQr: Boolean = false,
    private val onClick: (Lecture) -> Unit,
    private val onQR: (Lecture) -> Unit
) :
    ListAdapter<Lecture, LectureAdapter.ViewHolder>(SubjectDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemLectureBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnBack.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
            binding.btnQr.setOnClickListener {
                onQR(getItem(adapterPosition))
            }
        }

        fun bind(item: Lecture) {
            val date = Utility.convertStringToDateTime(
                item.date + " " + item.time,
                "dd-MM-yyyy hh:mm"
            )
            if (date.time < Date().time - (2 * 60 * 60 * 1001L)) {
                binding.btnQr.setImageResource(R.drawable.attend_list)
            }

            if (showQr){
                binding.lytQr.visibility = View.VISIBLE
                val lecture = Utility.convertStringToDateTime(
                    item.date + " " + item.time,
                    "dd-MM-yyyy hh:mm"
                )
                if (lecture.time  + (3 * 60 * 60 * 1001L) > Date().time && lecture.time - (10 * 60 * 1001L) <= Date().time)  {
                    binding.lytQr.visibility = View.VISIBLE
                } else
                    binding.lytQr.visibility = View.VISIBLE
            }

            binding.txtSubjectName.text = String.format("%s \\", item.subject.name)
            binding.txtLectureName.text = item.title
            binding.txtTeacherName.text = String.format("Teacher: %s", item.subject.teacher.name)
            binding.txtLectureTime.text = String.format("Start at: %s", item.time)
            binding.txtLectureDate.text = String.format("Date: %s", item.date)
        }
    }
}

object SubjectDiffCallback : DiffUtil.ItemCallback<Lecture>() {
    override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
        return oldItem.title == newItem.title
                && oldItem.time == newItem.time
                && oldItem.date == newItem.date
                && oldItem.subject == newItem.subject
                && oldItem.subjectID == newItem.subjectID
    }
}
