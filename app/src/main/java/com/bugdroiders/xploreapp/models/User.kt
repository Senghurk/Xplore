package com.bugdroiders.xploreapp.models

data class User(
    val email: String? = null,
    val fullName: String? = null,
    val userType: UserType? = null,
    val imageUrl: String? = null
)
