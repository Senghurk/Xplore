package com.bugdroiders.xploreapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Guide (
    @DocumentId val id:String? = null,
    val image: String? = "",
    val name: String?= "",
    val introduction: String? = "",
    val location: String? = "",
    val languages: List<String>? = null,
    val rating: Float? = 0.0f,
    val reviewCount: Int? = 0,
    val email: String? = null,
    val shortDescription:String? = null,
    val totalRating: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(introduction)
        parcel.writeString(location)
        parcel.writeStringList(languages)
        parcel.writeValue(rating)
        parcel.writeValue(reviewCount)
        parcel.writeString(email)
        parcel.writeString(shortDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Guide> {
        override fun createFromParcel(parcel: Parcel): Guide {
            return Guide(parcel)
        }

        override fun newArray(size: Int): Array<Guide?> {
            return arrayOfNulls(size)
        }
    }

}