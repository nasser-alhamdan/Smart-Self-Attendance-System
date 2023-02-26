package com.attend.data.models.sections

import androidx.annotation.Keep
import com.attend.data.models.users.Teacher
import java.io.Serializable

@Keep
data class Subject(
    var id: String = "",
    var name: String,
    var code: String,
    var teacher: Teacher,
    var teacherID: String,
    var section: Section,
    var sectionID: String,

    // TODO: Add number of Lectures?!
) : Serializable