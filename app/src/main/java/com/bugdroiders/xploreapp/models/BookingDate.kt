package com.bugdroiders.xploreapp.models

data class BookingDate (
    val date: Int,
    val month: String,
    val isAvailable: Boolean = true
)