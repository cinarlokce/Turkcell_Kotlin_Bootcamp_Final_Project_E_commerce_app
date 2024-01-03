package com.works.final_exam.models

import java.io.Serializable


data class JWTUser (
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val token: String
) : Serializable
