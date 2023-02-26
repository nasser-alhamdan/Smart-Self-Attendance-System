package com.attend.views.activities.home.ui.sections.lecture

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
import com.attend.databinding.FragmentLecturesBinding
import com.attend.views.activities.home.ui.qr.QRFragment
import com.attend.views.activities.home.ui.users.teacher.AttendedStudentsDialog
import java.util.*

class LecturesFragment : BaseFragment() {

    private var _binding: FragmentLecturesBinding? = null
    private val binding get() = _binding!!
    private var subjectID: String = ""
    private var studentID: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentLecturesBinding.inflate(inflater, container, false)
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
            if (container?.up?.student == null) {
                fab.setOnClickListener {
                    startActivity(
                        Intent(requireContext(), LectureActivity::class.java)
                            .putExtra("subjectID", subjectID)
                    )
                }
            } else fab.visibility = View.GONE

            adapter = LectureAdapter(showQr = true,
                onClick = {
                    if (container?.up?.student == null) {
                        val date = Utility.convertStringToDateTime(
                            it.date + " " + it.time,
                            "dd-MM-yyyy hh:mm"
                        )
                        if (date.time >= Date().time - (2 * 60 * 60 * 1001L)) {
                            startActivity(
                                Intent(requireContext(), LectureActivity::class.java)
                                    .putExtra("lectureID", it.id)
                                    .putExtra("subjectID", subjectID)
                                    .putExtra("updatedAccount", true)
                            )
                        }
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
        subjectID = (activity as AppCompatActivity).intent.getStringExtra("subjectID").toString()
        getSubject(subjectID)
        getLectures(subjectID)
    }

    private fun getSubject(subjectID: String) {
        container?.repository?.getSubject(subjectID) {
            (activity as AppCompatActivity).supportActionBar?.title = it?.name
        }
    }

    private fun getLectures(subjectID: String?) {
        container?.repository?.getLectures(subjectID = subjectID) {
            it.sortBy { it.date }
            adapter.submitList(it)
        }
    }
}