package de.fbirk.doubleout.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class TimeLineModel (var message: String, var date: String) : Parcelable