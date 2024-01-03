package com.works.final_exam.models

data class UpdateUserRequest(
    var firstName: String,
    var lastName: String,
    var email: String,
    var username: String
)
