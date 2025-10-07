package com.bugdroiders.xploreapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Tour(
    @DocumentId val id:String? = null,
    val image: String? = null,
    val title: String? = null,
    val description: String? = null,
    val rating: Float? = 0.0f,
    val reviewCount: Int? = 0,
    val tourLocation: String? = "",
    val tourType: String? = "",
    val price: Int? = 0,
    val guide: String? = "",
    val guideId: String? = "",
    val guideImage: String? = "",
    val tourDuration: String? = "",
    val schedule: List<Schedule>? = null,
    val totalRating: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
//        TODO("schedule"),
//        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(rating)
        parcel.writeValue(reviewCount)
        parcel.writeString(tourLocation)
        parcel.writeString(tourType)
        parcel.writeValue(price)
        parcel.writeString(guide)
        parcel.writeString(guideId)
        parcel.writeString(guideImage)
        parcel.writeString(tourDuration)
        parcel.writeValue(totalRating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tour> {
        override fun createFromParcel(parcel: Parcel): Tour {
            return Tour(parcel)
        }

        override fun newArray(size: Int): Array<Tour?> {
            return arrayOfNulls(size)
        }
    }

}
