package com.attend.data.models.sections

import androidx.annotation.Keep
import com.attend.data.models.users.Student
import java.io.Serializable

@Keep
data class SubjectStudent(
    var id: String = "",
    var subject: Subject?,
    var subjectID: String = "",
    var student: Student?,
    var studentID: String = "",
) : Serializable