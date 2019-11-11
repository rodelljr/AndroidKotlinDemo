package com.kotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(var location: String, var latitude: Double, var longitude: Double) : Parcelable {

    // Need to override to get only the location in the Spinner Control
    override fun toString(): String {
        return location
    }
}