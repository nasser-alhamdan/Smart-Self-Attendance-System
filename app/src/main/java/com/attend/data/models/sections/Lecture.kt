package com.attend.data.models.sections

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Lecture(
    var id: String = "",
    var title: String, // Lecture Title
    var date: String,
    var time: String,
    var timestamp: Long,

    var subject: Subject,
    var subjectID: String,
) : Serializable