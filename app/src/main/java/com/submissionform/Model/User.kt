package com.submissionform.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User
    (
    var username: String? = "",
    var password: String? = "",
    var name: String? = "",
    var userEnabled: Boolean? = true,
    var userid:String? = ""

)