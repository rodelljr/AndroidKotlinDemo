package com.kotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(val response: ArrayList<ResponseModel>) : Parcelable

@Parcelize
data class ResponseModel(val message: String, val duration: Long, val risetime: Long) : Parcelable
