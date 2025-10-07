package com.bugdroiders.xploreapp.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Review(
    @DocumentId val id:String? = null,
    val reviewText: String? = "",
    val rating: Int  = 0,
    val reviewerName: String? = "",
    val reviewerProfile: String? = "",
    val reviewDate: Date? = null,
    val tourId: String? = null,
    val guideId: String? = null
)
