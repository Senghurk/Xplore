package com.bugdroiders.xploreapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Booking(
    @DocumentId val id:String? = null,
    val travelerId: String? = "",
    val travelerName: String? = "",
    val guideId: String? = "",
    val guideName: String? = "",
    val reviewed: Boolean? = false,
    val tourId: String? = "",
    val tourName: String? = "",
    val bookingDate: Date? = Date(),
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(travelerId)
        parcel.writeString(travelerName)
        parcel.writeString(guideId)
        parcel.writeString(guideName)
        parcel.writeValue(reviewed)
        parcel.writeString(tourId)
        parcel.writeString(tourName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }

}
