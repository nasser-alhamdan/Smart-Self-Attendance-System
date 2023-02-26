package com.attend.data.models.sections

import androidx.annotation.Keep
import com.attend.common.util.LectureAttendingStates
import com.attend.data.models.users.Student
import java.io.Serializable
import java.sql.Timestamp

@Keep
data class LectureAttendance(
    var id: String = "",
    var lectureID: String,
    var lecture: Lecture,
    var studentID: String,
    var student: Student,
    var subjectID: String,
    var timestamp: Long,

    var states: String = LectureAttendingStates.PENDING,  // PENDING, DONE, ABSENT
) : Serializable