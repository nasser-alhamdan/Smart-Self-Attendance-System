package com.attend.views.activities.home.ui.users.student

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.R
import com.attend.common.base.BaseFragment
import com.attend.common.util.Utility
import com.attend.data.models.sections.Lecture
import com.attend.databinding.FragmentStudentLecturesBinding
import com.attend.views.activities.home.ui.qr.ScannerActivity
import com.attend.views.activities.home.ui.sections.lecture.LectureAdapter
import java.util.*

class StudentLecturesFragment : BaseFragment() {

    private var _binding: FragmentStudentLecturesBinding? = null
    private val binding get() = _binding!!
    private var studentID: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentStudentLecturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private lateinit var adapter: LectureAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studentID = container?.up?.student?.id

        if (studentID != null) {
            (activity as AppCompatActivity).supportActionBar?.title =
                String.format(getString(R.string.lectures))
        }

        binding.apply {

            fabQr.setOnClickListener {
                // TODO: Make sure of Time and Date before updating
                // TODO: Make sure lectureID equality
                // TODO: Make sure of Time and Date if beyond make him Absent
                startActivity(Intent(requireContext(), ScannerActivity::class.java))
            }

            adapter = LectureAdapter(showQr = true,
                onClick = {},
                onQR = {
                    startActivity(
                        Intent(requireContext(), ScannerActivity::class.java)
                            .putExtra("lectureID", it)
                    )
                }
            )

            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter

        }

    }

    private fun showClassOnMap(context: Context, lat: Double, lng: Double) {
        val uri = "http://maps.google.com/maps?daddr=$lat,$lng"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        studentID?.let { getSubject(it) }
    }

    private fun getSubject(studentID: String) {
        container?.repository?.getSubjectStudents(studentID = studentID) {
            val subjectIDs = it.map { i ->
                i.subjectID
            }
            getLectures(subjectIDs)
        }
    }

    private fun getLectures(subjectIDs: List<String>) {
        val inIDs = ArrayList<String>()
        val newLectures = ArrayList<Lecture>()
        if (subjectIDs.isEmpty()) return
        val date = Utility.convertTimestampToString(Date().time, "dd-MM-yyyy")
        container?.repository?.getLectures(subjectIDs = subjectIDs, date = date) {
            if (it.size != 0) {
                it.forEach { item ->
                    val date = Utility.convertStringToDateTime(
                        item.date + " " + item.time,
                        "dd-MM-yyyy hh:mm"
                    )
                    if (date.time > Date().time - (3 * 60 * 60 * 1001L)) {
                        inIDs.add(item.id)
                    }
                }
            }
            it.forEach { lecture ->
                inIDs.forEach { inID ->
                    if (lecture.id == inID) {
                        newLectures.add(lecture)
                    }
                }
            }
            newLectures.sortBy { it.date }
            adapter.submitList(newLectures)
        }
    }
}