package com.attend.data.models.sections

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Section(
    var id: String = "",
    var name: String,
    var code: String,

    // TODO: Add Subjects?!
) : Serializable