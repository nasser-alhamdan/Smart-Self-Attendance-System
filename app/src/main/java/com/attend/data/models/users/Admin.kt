package com.attend.data.models.users

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Admin(
    var id: String = "",
    var name: String,
    var phone: String,
    var password: String,
) : Serializable