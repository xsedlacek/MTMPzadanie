package com.example.mtmpzadanie

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class CoordinateData(val x: Double, val y: Double, val t: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(x)
        parcel.writeDouble(y)
        parcel.writeDouble(t)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoordinateData> {
        override fun createFromParcel(parcel: Parcel): CoordinateData {
            return CoordinateData(parcel)
        }

        override fun newArray(size: Int): Array<CoordinateData?> {
            return arrayOfNulls(size)
        }
    }
}
