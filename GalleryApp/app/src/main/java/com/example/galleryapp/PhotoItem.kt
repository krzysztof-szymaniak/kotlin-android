package com.example.galleryapp

import android.os.Parcel
import android.os.Parcelable

data class PhotoItem(
    val src: String?,
    var description: String?,
    var rates: Float
    ) : Parcelable {
    // overrides beneath
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(src)
        parcel.writeString(description)
        parcel.writeFloat(rates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoItem> {
        override fun createFromParcel(parcel: Parcel): PhotoItem {
            return PhotoItem(parcel)
        }

        override fun newArray(size: Int): Array<PhotoItem?> {
            return arrayOfNulls(size)
        }
    }
}



