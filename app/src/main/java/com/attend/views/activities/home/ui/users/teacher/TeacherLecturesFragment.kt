package com.attend.views.activities.home.ui.users.teacher

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.attend.R
import com.attend.common.base.BaseFragment
import com.attend.common.util.Utility
import com.attend.databinding.FragmentTeacherLecturesBinding
import com.attend.views.activities.home.ui.qr.QRFragment
import com.attend.views.activities.home.ui.sections.lecture.LectureAdapter
import java.util.*

class TeacherLecturesFragment : BaseFragment() {

    private var _binding: FragmentTeacherLecturesBinding? = null
    private val binding get() = _binding!!
    private var teacherID: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentTeacherLecturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private lateinit var adapter: LectureAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teacherID = container?.up?.teacher?.id

        if (teacherID != null) {
            (activity as AppCompatActivity).supportActionBar?.title =
                String.format(getString(R.string.today_lectures))
        }

        binding.apply {
            adapter = LectureAdapter(showQr = true,
                onClick = { it ->
                    AttendedStudentsDialog.view(requireContext(), it.id) {
                        container?.up?.showToastMsg(requireContext(), "Student ID: ${it.univID}")
                    }
                }, onQR = {
                    val date = Utility.convertStringToDateTime(
                        it.date + " " + it.time,
                        "dd-MM-yyyy hh:mm"
                    )
                    if (date.time < Date().time - (2 * 60 * 60 * 1001L)) {
                        AttendedStudentsDialog.view(requireContext(), it.id) {
                            container?.up?.showToastMsg(
                                requireContext(),
                                "Student ID: ${it.univID}"
                            )
                        }
                    } else {
                        QRFragment.view(requireContext(), "${it.title} \nLecture QR", it.id) {}
                    }
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
        teacherID?.let { getSubject(it) }
    }

    private fun getSubject(teacherID: String) {
        container?.repository?.getSubjects(teacherID = teacherID) {
            val subjectIDs = it.map { item ->
                item.id
            }
            getLectures(subjectIDs)
        }
    }

    private fun getLectures(subjectIDs: List<String>) {
        val date = Utility.convertTimestampToString(
            Date().time,
            "d-MM-yyyy"
        )
        if (subjectIDs.isEmpty()) return
        container?.repository?.getLectures(subjectIDs = subjectIDs, date = date) {
            it.sortByDescending { it.date }
            adapter.submitList(it)
        }
    }
}