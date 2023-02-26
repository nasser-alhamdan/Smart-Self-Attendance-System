package com.attend.views.activities.home.ui.qr

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.attend.common.base.BaseActivity
import com.attend.common.util.Alert
import com.attend.common.util.LectureAttendingStates
import com.attend.common.util.Utility
import com.attend.data.models.sections.Lecture
import com.attend.data.models.sections.LectureAttendance
import com.attend.data.models.users.Student
import com.attend.databinding.ActivityScannerBinding
import com.budiyev.android.codescanner.*
import java.util.*


class ScannerActivity : BaseActivity() {
    private lateinit var binding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner
    private var lectureID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lecture QR Reader"

        lectureID = intent.getStringExtra("lectureID") ?: ""

        codeScanner = CodeScanner(this, binding.scannerView).apply {
            // Parameters (default values)
            camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    setResult(it.text)
                }
            }
            errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                runOnUiThread {
                    Alert.alert(
                        this@ScannerActivity,
                        "Camera initialization error: ${it.message}"
                    )
                }
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        requestPermissions()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
            return
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // If request is cancelled, the result arrays are empty.
        permissions.entries.forEach {
            if (it.value) {
                codeScanner.startPreview()
                return@forEach
            }
        }
    }

    var student: Student? = null
    private fun setResult(barcode: String) {
        if (barcode != "") {
            student = container?.up?.student
            getLecture(barcode)
            binding.root.postDelayed({
                lecture.let {
                    val lecture1 = Utility.convertStringToDateTime(
                        lecture?.date + " " + lecture?.time,
                        "dd-MM-yyyy hh:mm"
                    )
                    if (Date().time > lecture1.time - (10 * 60 *1001L) && Date().time < lecture1.time + (2 * 60 * 60 * 1000L)) {
                        if (Date().time - (2 * 60 * 60 * 1001L) < lecture1.time )  {
                            checkLectureAttendanceExist(barcode, student)
                        } else{
                            container?.repository?.checkLectureAttendanceExist(
                                lectureID = lecture?.id,
                                studentID = student?.id
                            ) {item ->
                                if (item != null){
                                    showStateMsg("You have Already Registered in this Lecture as ( Present ).")
                                }
                            }
                        }
                    } else{
                        if (Date().time < lecture1.time - (10 * 60 *1001L))
                            showStateMsg("You can\'t Register Attendance now, please wait until Lecture time.")
                        else
                            showStateMsg("You are too late to Register your Attendance to this Lecture any more.")
                    }
                }
            }, 1500)
        } else {
            Alert.toast(this, "Barcode not equals: ($barcode)")
        }
    }

    var lecture: Lecture? = null
    private fun checkLectureAttendanceExist(lectureID: String, student: Student?) {
        val lectureAttendance = lecture?.let {
            LectureAttendance(
                lectureID = lectureID,
                lecture = it,
                studentID = student?.id!!,
                student = student,
                subjectID = it.subjectID,
                timestamp = Date().time
            )
        }

        container?.repository?.checkLectureAttendanceExist(
            lectureID = lectureID,
            studentID = student?.id
        ) { item ->
            if (item == null) {
                lectureAttendance?.states = LectureAttendingStates.PRESENT
                markStudentAsPresent(lectureAttendance!!)
            } else if (item.states != LectureAttendingStates.PENDING) {
                if (item.states == LectureAttendingStates.PRESENT) {
                    showStateMsg("You have Already Registered in this Lecture as ( Present ).")
                } else if (item.states == LectureAttendingStates.ABSENT) {
                    showStateMsg("You were Registered as ( Absent ) in this Lecture.")
                }
            }
        }
    }

    private fun getLecture(lectureID: String) {
        container?.repository?.getLecture(lectureID) {
            if (it != null) {
                lecture = it
            }
        }
    }


    private fun markStudentAsPresent(lectureAttendance: LectureAttendance) {
        lectureAttendance.states = LectureAttendingStates.PRESENT
        container?.repository?.insertLectureAttendance(lectureAttendance) { LAID ->
            lectureAttendance.id = LAID
            container?.repository?.updateLectureAttendance(lectureAttendance) {
                if (it) {
                    AlertDialog.Builder(this).setTitle(lectureAttendance.student.name)
                        .setMessage("Attendance Registered for this Lecture successfully.")
                        .setPositiveButton("Done") { _: DialogInterface, _: Int ->
                            finish()
                        }.show()
                } else
                    Alert.toast(this, "Something went wrong, please try again later.")
            }
        }
    }

    private fun showStateMsg(msg: String) {
        AlertDialog.Builder(this).setTitle("Lecture Attendance")
            .setMessage(msg)
            .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                finish()
            }.show()
    }
}