package com.kotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseModel(val message: String, val duration: Long, val risetime: Long): Parcelable