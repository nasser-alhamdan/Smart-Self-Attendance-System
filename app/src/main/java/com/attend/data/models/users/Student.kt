package com.attend.data.models.users

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Student(
    var id: String = "",
    var univID: String,
    var name: String,
    var phone: String,
    var password: String,


    var timestamp: Long = -1
) : Serializable